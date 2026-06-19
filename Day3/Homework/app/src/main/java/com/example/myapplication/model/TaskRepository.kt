package com.example.myapplication.model

import com.example.myapplication.data.CreateTaskRequest
import com.example.myapplication.data.PutTaskRequest
import com.example.myapplication.data.TaskDataSource
import com.example.myapplication.data.TaskDto
import com.example.myapplication.data.local.TaskEntity
import com.example.myapplication.data.local.TaskLocalDataSource
import com.example.myapplication.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

interface TaskRepositoryContract {
    val tasksFlow: Flow<List<Task>>

    suspend fun login(username: String, password: String): TaskResult<Unit>
    suspend fun loadTasks(): TaskResult<List<Task>>
    suspend fun getTask(taskId: String): TaskResult<Task>
    suspend fun createTask(title: String, body: String): TaskResult<Task>
    suspend fun updateTask(taskId: String, title: String, body: String): TaskResult<Task>
    suspend fun deleteTask(task: Task): TaskResult<Unit>
    suspend fun toggleTaskCompletion(taskId: String)
}

class TaskRepository(
    private val remoteDataSource: TaskDataSource,
    private val localDataSource: TaskLocalDataSource,
    private val logger: Logger
) : TaskRepositoryContract {
    override val tasksFlow: Flow<List<Task>> = localDataSource.observeTasks()
        .map { tasks -> tasks.map { task -> task.toTask() } }

    private var authToken: String? = null

    override suspend fun login(username: String, password: String): TaskResult<Unit> = runApiCall {
        logger.info("Login requested")
        authToken = remoteDataSource.login(username, password).token
        logger.info("Login succeeded")
    }

    override suspend fun loadTasks(): TaskResult<List<Task>> = runApiCall {
        logger.debug("Loading task list")
        val localTasks = localDataSource.observeTasks().first()
        val completionById = localTasks.associate { task -> task.id to task.isCompleted }
        val loadedTasks = remoteDataSource.getTasks(authorizationHeader())
            .tasks
            .map { taskDto ->
                taskDto.toEntity(
                    isCompleted = completionById[taskDto.id] ?: false,
                    isPendingSync = false
                )
            }

        localDataSource.replaceSyncedTasks(loadedTasks)
        logger.debug("Loaded ${loadedTasks.size} tasks")
        loadedTasks.map { task -> task.toTask() }
    }

    override suspend fun getTask(taskId: String): TaskResult<Task> = runApiCall {
        logger.debug("Loading task id=$taskId")
        val localTask = localDataSource.getTask(taskId)
        if (localTask != null) {
            return@runApiCall localTask.toTask()
        }

        val remoteTask = remoteDataSource.getTask(authorizationHeader(), taskId).toEntity(
            isCompleted = false,
            isPendingSync = false
        )
        localDataSource.upsertTask(remoteTask)
        remoteTask.toTask()
    }

    override suspend fun createTask(title: String, body: String): TaskResult<Task> {
        logger.debug("Creating task")
        return try {
            val localTask = withContext(Dispatchers.IO) {
                val task = TaskEntity(
                    id = "local-${UUID.randomUUID()}",
                    title = title,
                    body = body,
                    username = "",
                    isRemoteEditable = false,
                    isCompleted = false,
                    isPendingSync = true,
                    updatedAt = System.currentTimeMillis()
                )
                localDataSource.upsertTask(task)
                task
            }

            try {
                val syncedTask = withContext(Dispatchers.IO) {
                    val response = remoteDataSource.createTask(
                        authorization = authorizationHeader(),
                        request = CreateTaskRequest(title = title, body = body)
                    )
                    val task = TaskEntity(
                        id = response.id,
                        title = title,
                        body = body,
                        username = "",
                        isRemoteEditable = true,
                        isCompleted = localTask.isCompleted,
                        isPendingSync = false,
                        updatedAt = System.currentTimeMillis()
                    )
                    localDataSource.deleteTask(localTask.id)
                    localDataSource.upsertTask(task)
                    task
                }
                logger.info("Created task id=${syncedTask.id}")
                TaskResult.Success(syncedTask.toTask())
            } catch (error: Exception) {
                logger.error("Remote create failed, keeping local task id=${localTask.id}", error)
                TaskResult.Success(localTask.toTask())
            }
        } catch (error: Exception) {
            TaskResult.Failure(error.message ?: "Something went wrong.")
        }
    }

    override suspend fun updateTask(taskId: String, title: String, body: String): TaskResult<Task> {
        logger.debug("Updating task id=$taskId")
        return try {
            val localTask = withContext(Dispatchers.IO) {
                val existingTask = localDataSource.getTask(taskId)
                val task = TaskEntity(
                    id = taskId,
                    title = title,
                    body = body,
                    username = existingTask?.username.orEmpty(),
                    isRemoteEditable = existingTask?.isRemoteEditable ?: true,
                    isCompleted = existingTask?.isCompleted ?: false,
                    isPendingSync = true,
                    updatedAt = System.currentTimeMillis()
                )
                localDataSource.upsertTask(task)
                task
            }

            if (taskId.startsWith(LOCAL_ID_PREFIX)) {
                return TaskResult.Success(localTask.toTask())
            }

            try {
                withContext(Dispatchers.IO) {
                    remoteDataSource.updateTask(
                        authorization = authorizationHeader(),
                        id = taskId,
                        request = PutTaskRequest(
                            title = title,
                            body = body
                        )
                    )
                    localDataSource.upsertTask(localTask.copy(isPendingSync = false))
                }
                logger.info("Updated task id=$taskId")
            } catch (error: Exception) {
                logger.error("Remote update failed, keeping local update id=$taskId", error)
            }

            TaskResult.Success(localTask.toTask())
        } catch (error: Exception) {
            TaskResult.Failure(error.message ?: "Something went wrong.")
        }
    }

    override suspend fun deleteTask(task: Task): TaskResult<Unit> {
        logger.debug("Deleting task id=${task.id}")
        return try {
            withContext(Dispatchers.IO) {
                localDataSource.deleteTask(task.id)
                if (!task.id.startsWith(LOCAL_ID_PREFIX)) {
                    remoteDataSource.deleteTask(authorizationHeader(), task.id)
                }
            }
            logger.info("Deleted task id=${task.id}")
            TaskResult.Success(Unit)
        } catch (error: Exception) {
            logger.error("Remote delete failed after local delete id=${task.id}", error)
            TaskResult.Success(Unit)
        }
    }

    override suspend fun toggleTaskCompletion(taskId: String) {
        logger.debug("Toggling completion for task id=$taskId")
        withContext(Dispatchers.IO) {
            val task = localDataSource.getTask(taskId) ?: return@withContext
            localDataSource.upsertTask(
                task.copy(
                    isCompleted = !task.isCompleted,
                    updatedAt = System.currentTimeMillis()
                )
            )
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

    private fun TaskDto.toEntity(isCompleted: Boolean, isPendingSync: Boolean): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            body = body,
            username = "",
            isRemoteEditable = true,
            isCompleted = isCompleted,
            isPendingSync = isPendingSync,
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun TaskEntity.toTask(): Task {
        return Task(
            id = id,
            title = title,
            body = body,
            username = username,
            isRemoteEditable = isRemoteEditable,
            isCompleted = isCompleted,
            isPendingSync = isPendingSync
        )
    }

    private companion object {
        const val LOCAL_ID_PREFIX = "local-"
    }
}

sealed interface TaskResult<out T> {
    data class Success<T>(val value: T) : TaskResult<T>
    data class Failure(val message: String) : TaskResult<Nothing>
}

private suspend inline fun <T> runApiCall(crossinline call: suspend () -> T): TaskResult<T> {
    return try {
        TaskResult.Success(withContext(Dispatchers.IO) { call() })
    } catch (error: Exception) {
        TaskResult.Failure(error.message ?: "Something went wrong.")
    }
}
