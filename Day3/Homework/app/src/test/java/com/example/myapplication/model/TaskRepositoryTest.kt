package com.example.myapplication.model

import com.example.myapplication.FakeLogger
import com.example.myapplication.data.CreateTaskRequest
import com.example.myapplication.data.CreateTaskResponse
import com.example.myapplication.data.GetAllTasksResponse
import com.example.myapplication.data.LoginResponse
import com.example.myapplication.data.PutTaskRequest
import com.example.myapplication.data.TaskDataSource
import com.example.myapplication.data.TaskDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskRepositoryTest {
    @Test
    fun loginThenLoadTasksSavesTokenAndPublishesTasks() = runTest {
        val dataSource = FakeTaskDataSource(
            remoteTasks = listOf(TaskDto(username = "ana", title = "Study", body = "Write tests"))
        )
        val repository = TaskRepository(dataSource, FakeLogger())

        repository.login(username = "ana", password = "secret")
        val result = repository.loadTasks()

        assertTrue(result is TaskResult.Success)
        assertEquals("Bearer token-123", dataSource.lastAuthorization)
        assertEquals("Study", repository.tasksFlow.value.first().title)
        assertEquals("Write tests", repository.tasksFlow.value.first().body)
    }

    @Test
    fun createTaskAddsCreatedTaskToFlow() = runTest {
        val repository = TaskRepository(FakeTaskDataSource(createdTaskId = 77), FakeLogger())

        repository.login(username = "ana", password = "secret")
        val result = repository.createTask(title = "Homework", body = "Finish testing task")

        assertTrue(result is TaskResult.Success)
        assertEquals(77, repository.tasksFlow.value.first().id)
        assertEquals("Homework", repository.tasksFlow.value.first().title)
    }

    @Test
    fun deleteTaskRemovesTaskFromFlow() = runTest {
        val repository = TaskRepository(FakeTaskDataSource(createdTaskId = 10), FakeLogger())

        repository.login(username = "ana", password = "secret")
        val createdTask = (repository.createTask("Clean", "Desk") as TaskResult.Success).value
        repository.deleteTask(createdTask)

        assertTrue(repository.tasksFlow.value.isEmpty())
    }

    @Test
    fun toggleTaskCompletionMarksTaskDone() = runTest {
        val repository = TaskRepository(FakeTaskDataSource(createdTaskId = 12), FakeLogger())

        repository.login(username = "ana", password = "secret")
        val createdTask = (repository.createTask("Read", "Testing slides") as TaskResult.Success).value
        repository.toggleTaskCompletion(createdTask.id)

        assertTrue(repository.tasksFlow.value.first().isCompleted)
    }
}

private class FakeTaskDataSource(
    private val remoteTasks: List<TaskDto> = emptyList(),
    private val createdTaskId: Int = 1
) : TaskDataSource {
    var lastAuthorization: String? = null

    override suspend fun login(username: String, password: String): LoginResponse {
        return LoginResponse(token = "token-123")
    }

    override suspend fun getTasks(authorization: String): GetAllTasksResponse {
        lastAuthorization = authorization
        return GetAllTasksResponse(tasks = remoteTasks)
    }

    override suspend fun createTask(
        authorization: String,
        request: CreateTaskRequest
    ): CreateTaskResponse {
        lastAuthorization = authorization
        return CreateTaskResponse(id = createdTaskId)
    }

    override suspend fun getTask(authorization: String, id: Int): TaskDto {
        lastAuthorization = authorization
        return remoteTasks.first()
    }

    override suspend fun updateTask(authorization: String, id: Int, request: PutTaskRequest) {
        lastAuthorization = authorization
    }

    override suspend fun deleteTask(authorization: String, id: Int) {
        lastAuthorization = authorization
    }
}
