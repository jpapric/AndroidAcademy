package com.example.myapplication.presentation

import com.example.myapplication.model.Task

data class TaskListScreenState(
    val tasks: List<Task> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val taskPendingDelete: Task? = null
)
