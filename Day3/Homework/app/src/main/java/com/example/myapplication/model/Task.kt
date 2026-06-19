package com.example.myapplication.model

data class Task(
    val id: String,
    val title: String,
    val body: String,
    val username: String = "",
    val isRemoteEditable: Boolean = true,
    val isCompleted: Boolean = false,
    val isPendingSync: Boolean = false
)
