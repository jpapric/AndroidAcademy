package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.model.TaskRepository
import com.example.myapplication.model.TaskResult
import com.example.myapplication.model.taskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskEditViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskEditScreenState())
    val uiState = _uiState.asStateFlow()

    fun startCreatingTask() {
        _uiState.value = TaskEditScreenState()
    }

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _uiState.value = TaskEditScreenState(loading = true)
            _uiState.value = when (val result = taskRepository.getTask(taskId)) {
                is TaskResult.Success -> TaskEditScreenState(
                    taskId = result.value.id,
                    title = result.value.title,
                    body = result.value.body,
                    editingExistingTask = true,
                    canUpdateRemoteTask = result.value.isRemoteEditable
                )
                is TaskResult.Failure -> TaskEditScreenState(
                    loading = false,
                    errorMessage = result.message
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title, errorMessage = null)
    }

    fun updateBody(body: String) {
        _uiState.value = _uiState.value.copy(body = body, errorMessage = null)
    }

    fun saveTask(onSaved: () -> Unit) {
        val currentState = _uiState.value
        val title = currentState.title.trim()
        val body = currentState.body.trim()

        if (title.isEmpty() || body.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = "Title and body are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(saving = true, errorMessage = null)

            val result = if (currentState.editingExistingTask) {
                val taskId = currentState.taskId
                if (taskId == null || !currentState.canUpdateRemoteTask) {
                    TaskResult.Failure("This task cannot be updated because the API did not return its id.")
                } else {
                    taskRepository.updateTask(taskId, title, body)
                }
            } else {
                taskRepository.createTask(title, body)
            }

            when (result) {
                is TaskResult.Success -> {
                    _uiState.value = TaskEditScreenState(
                        taskId = result.value.id,
                        title = result.value.title,
                        body = result.value.body,
                        editingExistingTask = true,
                        canUpdateRemoteTask = result.value.isRemoteEditable
                    )
                    onSaved()
                }
                is TaskResult.Failure -> _uiState.value = currentState.copy(
                    saving = false,
                    errorMessage = result.message
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                TaskEditViewModel(taskRepository) as T
        }
    }
}
