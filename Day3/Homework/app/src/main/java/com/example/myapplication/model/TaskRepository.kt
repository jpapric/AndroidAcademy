package com.example.myapplication.model

import com.example.myapplication.data.CreateTaskRequest
import com.example.myapplication.data.PutTaskRequest
import com.example.myapplication.data.TaskRemoteDataSource
import com.example.myapplication.data.TaskDto
import com.example.myapplication.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskRepository(
    private val remoteDataSource: TaskRemoteDataSource,
    private val logger: Logger
) {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasksFlow = _tasks.asStateFlow()

    private var authToken: String? = null
    private val knownTaskIds = mutableMapOf<TaskKey, Int>()

    suspend fun login(username: String, password: String): TaskResult<Unit> = runApiCall {
        logger.info("Login requested")
        authToken = remoteDataSource.login(username, password).token
        logger.info("Login succeeded")
    }

    suspend fun loadTasks(): TaskResult<List<Task>> = runApiCall {
        logger.debug("Loading task list")
        val loadedTasks = remoteDataSource.getTasks(authorizationHeader())
            .tasks
            .mapIndexed { index, taskDto -> taskDto.toTask(index) }

        _tasks.value = loadedTasks
        logger.debug("Loaded ${loadedTasks.size} tasks")
        loadedTasks
    }

    suspend fun getTask(taskId: Int): TaskResult<Task> = runApiCall {
        if (taskId < 0) {
            logger.debug("Loading local task placeholder id=$taskId")
            return@runApiCall _tasks.value.first { it.id == taskId }
        }

        logger.debug("Loading remote task id=$taskId")
        remoteDataSource.getTask(authorizationHeader(), taskId).toTaskWithId(taskId)
    }

    suspend fun createTask(title: String, body: String): TaskResult<Task> = runApiCall {
        logger.debug("Creating task")
        val response = remoteDataSource.createTask(
            authorization = authorizationHeader(),
            request = CreateTaskRequest(title = title, body = body)
        )
        val task = Task(
            id = response.id,
            title = title,
            body = body
        )
        knownTaskIds[TaskKey(task.title, task.body, task.username)] = task.id
        _tasks.value = _tasks.value + task
        logger.info("Created task id=${task.id}")
        task
    }

    suspend fun updateTask(taskId: Int, title: String, body: String): TaskResult<Task> = runApiCall {
        require(taskId >= 0) { "This task cannot be updated because the API did not return its id." }

        logger.debug("Updating task id=$taskId")
        remoteDataSource.updateTask(
            authorization = authorizationHeader(),
            id = taskId,
            request = PutTaskRequest(
                id = taskId,
                title = title,
                body = body
            )
        )
        val task = Task(id = taskId, title = title, body = body)
        _tasks.value = _tasks.value.map { if (it.id == taskId) task else it }
        logger.info("Updated task id=$taskId")
        task
    }

    suspend fun deleteTask(task: Task): TaskResult<Unit> = runApiCall {
        require(task.isRemoteEditable) { "This task cannot be deleted because the API did not return its id." }

        logger.debug("Deleting task id=${task.id}")
        remoteDataSource.deleteTask(authorizationHeader(), task.id)
        _tasks.value = _tasks.value.filterNot { it.id == task.id }
        logger.info("Deleted task id=${task.id}")
    }

    private fun authorizationHeader(): String {
        val token = authToken ?: throw IllegalStateException("You need to log in first.")
        return "Bearer $token"
    }

    private fun TaskDto.toTask(index: Int): Task {
        val knownId = knownTaskIds[TaskKey(title, body, username)]
        return Task(
            id = knownId ?: -(index + 1),
            title = title,
            body = body,
            username = username,
            isRemoteEditable = knownId != null
        )
    }

    private fun TaskDto.toTaskWithId(id: Int): Task {
        return Task(
            id = id,
            title = title,
            body = body,
            username = username,
            isRemoteEditable = true
        )
    }
}

sealed interface TaskResult<out T> {
    data class Success<T>(val value: T) : TaskResult<T>
    data class Failure(val message: String) : TaskResult<Nothing>
}

private suspend inline fun <T> runApiCall(crossinline call: suspend () -> T): TaskResult<T> {
    return try {
        TaskResult.Success(call())
    } catch (error: Exception) {
        TaskResult.Failure(error.message ?: "Something went wrong.")
    }
}

private data class TaskKey(
    val title: String,
    val body: String,
    val username: String
)
