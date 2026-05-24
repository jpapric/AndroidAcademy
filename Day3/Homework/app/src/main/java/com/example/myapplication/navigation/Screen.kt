package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object List : Screen("list")
    object Edit : Screen("edit")
}
