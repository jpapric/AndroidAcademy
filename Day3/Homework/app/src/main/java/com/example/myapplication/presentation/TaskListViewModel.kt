package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskRepository
import com.example.myapplication.model.TaskResult
import com.example.myapplication.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskRepository: TaskRepository,
    private val logger: Logger
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            taskRepository.tasksFlow.collect { tasks ->
                _uiState.value = _uiState.value.copy(tasks = tasks)
            }
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            logger.info("Loading tasks")
            _uiState.value = _uiState.value.copy(loading = true, errorMessage = null)
            _uiState.value = when (val result = taskRepository.loadTasks()) {
                is TaskResult.Success -> {
                    logger.info("Tasks loaded: ${result.value.size}")
                    _uiState.value.copy(
                        loading = false,
                        tasks = result.value
                    )
                }
                is TaskResult.Failure -> {
                    logger.error("Task list loading failed: ${result.message}")
                    _uiState.value.copy(
                        loading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun showDeleteConfirmation(task: Task) {
        logger.debug("Showing delete confirmation for task id=${task.id}")
        _uiState.value = _uiState.value.copy(taskPendingDelete = task)
    }

    fun dismissDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(taskPendingDelete = null)
    }

    fun deletePendingTask() {
        val task = _uiState.value.taskPendingDelete ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                taskPendingDelete = null,
                errorMessage = null
            )
            when (val result = taskRepository.deleteTask(task)) {
                is TaskResult.Success -> logger.info("Deleted task id=${task.id}")
                is TaskResult.Failure -> _uiState.value = _uiState.value.copy(
                    errorMessage = result.message
                )
            }
        }
    }

    companion object {
        fun factory(
            taskRepository: TaskRepository,
            logger: Logger
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return TaskListViewModel(taskRepository, logger) as T
            }
        }
    }
}
