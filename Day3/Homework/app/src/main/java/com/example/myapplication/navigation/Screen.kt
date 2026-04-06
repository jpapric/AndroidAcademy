package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Edit : Screen("edit/{id}") {
        fun createRoute(id: Int) = "edit/$id"
    }
}