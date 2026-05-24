package com.example.myapplication.presentation

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null
)
