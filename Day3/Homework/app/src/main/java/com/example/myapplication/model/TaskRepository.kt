package com.example.myapplication.model

import com.example.myapplication.data.CreateTaskRequest
import com.example.myapplication.data.PutTaskRequest
import com.example.myapplication.data.TaskDataSource
import com.example.myapplication.data.TaskDto
import com.example.myapplication.util.Logger
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TaskRepositoryContract {
    val tasksFlow: StateFlow<List<Task>>

    suspend fun login(username: String, password: String): TaskResult<Unit>
    suspend fun loadTasks(): TaskResult<List<Task>>
    suspend fun getTask(taskId: String): TaskResult<Task>
    suspend fun createTask(title: String, body: String): TaskResult<Task>
    suspend fun updateTask(taskId: String, title: String, body: String): TaskResult<Task>
    suspend fun deleteTask(task: Task): TaskResult<Unit>
    fun toggleTaskCompletion(taskId: String)
}

class TaskRepository(
    private val remoteDataSource: TaskDataSource,
    private val logger: Logger
) : TaskRepositoryContract {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    override val tasksFlow = _tasks.asStateFlow()

    private var authToken: String? = null

    override suspend fun login(username: String, password: String): TaskResult<Unit> = runApiCall {
        logger.info("Login requested")
        authToken = remoteDataSource.login(username, password).token
        logger.info("Login succeeded")
    }

    override suspend fun loadTasks(): TaskResult<List<Task>> = runApiCall {
        logger.debug("Loading task list")
        val completionById = _tasks.value.associate { task -> task.id to task.isCompleted }
        val loadedTasks = remoteDataSource.getTasks(authorizationHeader())
            .tasks
            .map { taskDto -> taskDto.toTask() }
            .map { task -> task.copy(isCompleted = completionById[task.id] ?: false) }

        _tasks.value = loadedTasks
        logger.debug("Loaded ${loadedTasks.size} tasks")
        loadedTasks
    }

    override suspend fun getTask(taskId: String): TaskResult<Task> = runApiCall {
        logger.debug("Loading remote task id=$taskId")
        remoteDataSource.getTask(authorizationHeader(), taskId).toTask()
    }

    override suspend fun createTask(title: String, body: String): TaskResult<Task> = runApiCall {
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
        _tasks.value = _tasks.value + task
        logger.info("Created task id=${task.id}")
        task
    }

    override suspend fun updateTask(taskId: String, title: String, body: String): TaskResult<Task> = runApiCall {
        logger.debug("Updating task id=$taskId")
        remoteDataSource.updateTask(
            authorization = authorizationHeader(),
            id = taskId,
            request = PutTaskRequest(
                title = title,
                body = body
            )
        )
        val existingTask = _tasks.value.firstOrNull { it.id == taskId }
        val task = Task(
            id = taskId,
            title = title,
            body = body,
            isCompleted = existingTask?.isCompleted ?: false
        )
        _tasks.value = _tasks.value.map { if (it.id == taskId) task else it }
        logger.info("Updated task id=$taskId")
        task
    }

    override suspend fun deleteTask(task: Task): TaskResult<Unit> = runApiCall {
        logger.debug("Deleting task id=${task.id}")
        remoteDataSource.deleteTask(authorizationHeader(), task.id)
        _tasks.value = _tasks.value.filterNot { it.id == task.id }
        logger.info("Deleted task id=${task.id}")
    }

    override fun toggleTaskCompletion(taskId: String) {
        logger.debug("Toggling completion for task id=$taskId")
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(isCompleted = !task.isCompleted)
            } else {
                task
            }
        }
    }

    private fun authorizationHeader(): String {
        val token = authToken ?: throw IllegalStateException("You need to log in first.")
        return "Bearer $token"
    }

    private fun TaskDto.toTask(): Task {
        return Task(
            id = id,
            title = title,
            body = body,
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
