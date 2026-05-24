package com.example.myapplication.data

import com.example.myapplication.util.Logger

class TaskRemoteDataSource(
    private val api: TaskieApi,
    private val logger: Logger
) {
    suspend fun login(username: String, password: String): LoginResponse {
        logger.info("Logging in user=$username")
        return api.login(LoginRequest(username = username, password = password))
    }

    suspend fun getTasks(authorization: String): GetAllTasksResponse {
        logger.debug("Fetching all tasks")
        return api.getTasks(authorization)
    }

    suspend fun createTask(authorization: String, request: CreateTaskRequest): CreateTaskResponse {
        logger.debug("Creating task title=${request.title}")
        return api.createTask(authorization, request)
    }

    suspend fun getTask(authorization: String, id: Int): TaskDto {
        logger.debug("Fetching task id=$id")
        return api.getTask(authorization, id)
    }

    suspend fun updateTask(authorization: String, id: Int, request: PutTaskRequest) {
        logger.debug("Updating task id=$id")
        api.updateTask(authorization, id, request)
    }

    suspend fun deleteTask(authorization: String, id: Int) {
        logger.debug("Deleting task id=$id")
        api.deleteTask(authorization, id)
    }
}
