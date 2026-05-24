package com.example.myapplication.data

import com.example.myapplication.util.Logger

interface TaskDataSource {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun getTasks(authorization: String): GetAllTasksResponse
    suspend fun createTask(authorization: String, request: CreateTaskRequest): CreateTaskResponse
    suspend fun getTask(authorization: String, id: Int): TaskDto
    suspend fun updateTask(authorization: String, id: Int, request: PutTaskRequest)
    suspend fun deleteTask(authorization: String, id: Int)
}

class TaskRemoteDataSource(
    private val api: TaskieApi,
    private val logger: Logger
) : TaskDataSource {
    override suspend fun login(username: String, password: String): LoginResponse {
        logger.info("Logging in user=$username")
        return api.login(LoginRequest(username = username, password = password))
    }

    override suspend fun getTasks(authorization: String): GetAllTasksResponse {
        logger.debug("Fetching all tasks")
        return api.getTasks(authorization)
    }

    override suspend fun createTask(authorization: String, request: CreateTaskRequest): CreateTaskResponse {
        logger.debug("Creating task title=${request.title}")
        return api.createTask(authorization, request)
    }

    override suspend fun getTask(authorization: String, id: Int): TaskDto {
        logger.debug("Fetching task id=$id")
        return api.getTask(authorization, id)
    }

    override suspend fun updateTask(authorization: String, id: Int, request: PutTaskRequest) {
        logger.debug("Updating task id=$id")
        api.updateTask(authorization, id, request)
    }

    override suspend fun deleteTask(authorization: String, id: Int) {
        logger.debug("Deleting task id=$id")
        api.deleteTask(authorization, id)
    }
}
