package com.example.myapplication.presentation

import com.example.myapplication.FakeLogger
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskRepositoryContract
import com.example.myapplication.model.TaskResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class TaskListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadTasksShowsLoadedTasks() = runTest {
        val task = Task(id = "task-1", title = "Test ViewModel", body = "Write a unit test")
        val repository = FakeTaskRepository(loadTasksResult = TaskResult.Success(listOf(task)))
        val viewModel = TaskListViewModel(repository, FakeLogger())

        viewModel.loadTasks()

        assertFalse(viewModel.uiState.value.loading)
        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(listOf(task), viewModel.uiState.value.tasks)
    }

    @Test
    fun deletePendingTaskCallsRepositoryAndClearsDialogState() = runTest {
        val task = Task(id = "task-2", title = "Delete me", body = "Long press flow")
        val repository = FakeTaskRepository()
        val viewModel = TaskListViewModel(repository, FakeLogger())

        viewModel.showDeleteConfirmation(task)
        viewModel.deletePendingTask()

        assertEquals(task, repository.deletedTask)
        assertNull(viewModel.uiState.value.taskPendingDelete)
    }

    @Test
    fun updateSearchQueryFiltersVisibleTasks() = runTest {
        val matchingTask = Task(id = "task-1", title = "Write tests", body = "Repository and ViewModel")
        val hiddenTask = Task(id = "task-2", title = "Buy coffee", body = "Morning errand")
        val repository = FakeTaskRepository(
            loadTasksResult = TaskResult.Success(listOf(matchingTask, hiddenTask))
        )
        val viewModel = TaskListViewModel(repository, FakeLogger())

        viewModel.loadTasks()
        viewModel.updateSearchQuery("tests")

        assertEquals(listOf(matchingTask), viewModel.uiState.value.visibleTasks)
    }

    @Test
    fun toggleTaskCompletionDelegatesToRepository() = runTest {
        val task = Task(id = "task-3", title = "Tap circle", body = "Mark as done")
        val repository = FakeTaskRepository()
        val viewModel = TaskListViewModel(repository, FakeLogger())

        viewModel.toggleTaskCompletion(task)

        assertEquals("task-3", repository.toggledTaskId)
    }
}

private class FakeTaskRepository(
    private val loadTasksResult: TaskResult<List<Task>> = TaskResult.Success(emptyList())
) : TaskRepositoryContract {
    override val tasksFlow: StateFlow<List<Task>> = MutableStateFlow(emptyList())
    var deletedTask: Task? = null

    override suspend fun login(username: String, password: String): TaskResult<Unit> {
        return TaskResult.Success(Unit)
    }

    override suspend fun loadTasks(): TaskResult<List<Task>> {
        return loadTasksResult
    }

    override suspend fun getTask(taskId: String): TaskResult<Task> {
        return TaskResult.Failure("Not needed")
    }

    override suspend fun createTask(title: String, body: String): TaskResult<Task> {
        return TaskResult.Failure("Not needed")
    }

    override suspend fun updateTask(taskId: String, title: String, body: String): TaskResult<Task> {
        return TaskResult.Failure("Not needed")
    }

    override suspend fun deleteTask(task: Task): TaskResult<Unit> {
        deletedTask = task
        return TaskResult.Success(Unit)
    }

    var toggledTaskId: String? = null

    override fun toggleTaskCompletion(taskId: String) {
        toggledTaskId = taskId
    }
}
