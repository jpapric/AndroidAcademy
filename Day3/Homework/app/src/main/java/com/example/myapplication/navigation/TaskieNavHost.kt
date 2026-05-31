package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.LoginScreenState
import com.example.myapplication.presentation.TaskEditScreenState
import com.example.myapplication.presentation.TaskListScreenState
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.screens.TaskEditScreen
import com.example.myapplication.screens.TaskListScreen

@Composable
fun TaskieNavHost(
    loginState: LoginScreenState,
    listState: TaskListScreenState,
    editState: TaskEditScreenState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (() -> Unit) -> Unit,
    onLoadTasks: () -> Unit,
    onAddClick: () -> Unit,
    onTaskClick: (String) -> Unit,
    onTaskLongClick: (com.example.myapplication.model.Task) -> Unit,
    onTaskStatusClick: (com.example.myapplication.model.Task) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSaveClick: (() -> Unit) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                state = loginState,
                onUsernameChange = onUsernameChange,
                onPasswordChange = onPasswordChange,
                onLogin = {
                    onLoginClick {
                        navController.navigateToList()
                        onLoadTasks()
                    }
                }
            )
        }

        composable(Screen.List.route) {
            TaskListScreen(
                tasks = listState.tasks,
                visibleTasks = listState.visibleTasks,
                completedTasks = listState.completedTasks,
                searchQuery = listState.searchQuery,
                loading = listState.loading,
                errorMessage = listState.errorMessage,
                taskPendingDelete = listState.taskPendingDelete,
                onAdd = {
                    onAddClick()
                    navController.navigate(Screen.Edit.route)
                },
                onRetry = onLoadTasks,
                onTaskClick = { task ->
                    onTaskClick(task.id)
                    navController.navigate(Screen.Edit.route)
                },
                onTaskLongClick = onTaskLongClick,
                onTaskStatusClick = onTaskStatusClick,
                onSearchQueryChange = onSearchQueryChange,
                onDismissDelete = onDismissDelete,
                onConfirmDelete = onConfirmDelete
            )
        }

        composable(Screen.Edit.route) {
            TaskEditScreen(
                state = editState,
                onTitleChange = onTitleChange,
                onBodyChange = onBodyChange,
                onSave = {
                    onSaveClick {
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun NavHostController.navigateToList() {
    navigate(Screen.List.route) {
        popUpTo(Screen.Login.route) {
            inclusive = true
        }
    }
}
