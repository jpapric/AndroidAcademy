package com.example.myapplication.model

import com.example.myapplication.data.CreateTaskRequest
import com.example.myapplication.data.NetworkModule
import com.example.myapplication.data.PutTaskRequest
import com.example.myapplication.data.TaskDto
import com.example.myapplication.data.TaskieApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val taskRepository by lazy {
    TaskRepository(NetworkModule.taskieApi)
}

class TaskRepository(
    private val api: TaskieApi
) {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasksFlow = _tasks.asStateFlow()

    private var authToken: String? = null
    private val knownTaskIds = mutableMapOf<TaskKey, Int>()

    suspend fun login(username: String, password: String): TaskResult<Unit> = runApiCall {
        authToken = api.login(
            com.example.myapplication.data.LoginRequest(
                username = username,
                password = password
            )
        ).token
    }

    suspend fun loadTasks(): TaskResult<List<Task>> = runApiCall {
        val loadedTasks = api.getTasks(authorizationHeader())
            .tasks
            .mapIndexed { index, taskDto -> taskDto.toTask(index) }

        _tasks.value = loadedTasks
        loadedTasks
    }

    suspend fun getTask(taskId: Int): TaskResult<Task> = runApiCall {
        if (taskId < 0) {
            return@runApiCall _tasks.value.first { it.id == taskId }
        }

        api.getTask(authorizationHeader(), taskId).toTaskWithId(taskId)
    }

    suspend fun createTask(title: String, body: String): TaskResult<Task> = runApiCall {
        val response = api.createTask(
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
        task
    }

    suspend fun updateTask(taskId: Int, title: String, body: String): TaskResult<Task> = runApiCall {
        require(taskId >= 0) { "This task cannot be updated because the API did not return its id." }

        api.updateTask(
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
        task
    }

    suspend fun deleteTask(task: Task): TaskResult<Unit> = runApiCall {
        require(task.isRemoteEditable) { "This task cannot be deleted because the API did not return its id." }

        api.deleteTask(authorizationHeader(), task.id)
        _tasks.value = _tasks.value.filterNot { it.id == task.id }
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
