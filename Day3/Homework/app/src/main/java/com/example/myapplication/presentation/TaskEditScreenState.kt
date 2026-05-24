package com.example.myapplication.presentation

data class TaskEditScreenState(
    val taskId: Int? = null,
    val title: String = "",
    val body: String = "",
    val editingExistingTask: Boolean = false,
    val saving: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val canUpdateRemoteTask: Boolean = true
)
