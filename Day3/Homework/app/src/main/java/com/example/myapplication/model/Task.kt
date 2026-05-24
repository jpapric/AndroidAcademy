package com.example.myapplication.model

data class Task(
    val id: Int,
    val title: String,
    val body: String,
    val username: String = "",
    val isRemoteEditable: Boolean = true
)
