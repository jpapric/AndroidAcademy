package com.example.myapplication.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskieApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("tasks/all")
    suspend fun getTasks(@Header("Authorization") authorization: String): GetAllTasksResponse

    @POST("tasks/create")
    suspend fun createTask(
        @Header("Authorization") authorization: String,
        @Body request: CreateTaskRequest
    ): CreateTaskResponse

    @GET("tasks/{id}")
    suspend fun getTask(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body request: PutTaskRequest
    )

    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    )
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class GetAllTasksResponse(
    val tasks: List<TaskDto>
)

data class TaskDto(
    val username: String,
    val title: String,
    val body: String
)

data class CreateTaskRequest(
    val title: String,
    val body: String
)

data class CreateTaskResponse(
    val id: Int
)

data class PutTaskRequest(
    val id: Int,
    val title: String,
    val body: String
)
