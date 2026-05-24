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

class LoginViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login(onLoggedIn: () -> Unit) {
        val currentState = _uiState.value
        val username = currentState.username.trim()
        val password = currentState.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = "Username and password are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(loading = true, errorMessage = null)
            when (val result = taskRepository.login(username, password)) {
                is TaskResult.Success -> {
                    _uiState.value = currentState.copy(loading = false)
                    onLoggedIn()
                }
                is TaskResult.Failure -> _uiState.value = currentState.copy(
                    loading = false,
                    errorMessage = result.message
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                LoginViewModel(taskRepository) as T
        }
    }
}
