package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myapplication.navigation.TaskieNavHost
import com.example.myapplication.presentation.LoginViewModel
import com.example.myapplication.presentation.TaskEditViewModel
import com.example.myapplication.presentation.TaskListViewModel
import com.example.myapplication.ui.theme.HomeworkTheme

class MainActivity : ComponentActivity() {
    private val appContainer by lazy {
        (application as TaskieApplication).appContainer
    }

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.factory(
            taskRepository = appContainer.taskRepository,
            logger = appContainer.loginViewModelLogger
        )
    }
    private val taskListViewModel: TaskListViewModel by viewModels {
        TaskListViewModel.factory(
            taskRepository = appContainer.taskRepository,
            logger = appContainer.taskListViewModelLogger
        )
    }
    private val taskEditViewModel: TaskEditViewModel by viewModels {
        TaskEditViewModel.factory(
            taskRepository = appContainer.taskRepository,
            logger = appContainer.taskEditViewModelLogger
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeworkTheme {
                val loginState by loginViewModel.uiState.collectAsState()
                val listState by taskListViewModel.uiState.collectAsState()
                val editState by taskEditViewModel.uiState.collectAsState()

                TaskieNavHost(
                    loginState = loginState,
                    listState = listState,
                    editState = editState,
                    onUsernameChange = loginViewModel::updateUsername,
                    onPasswordChange = loginViewModel::updatePassword,
                    onLoginClick = loginViewModel::login,
                    onLoadTasks = taskListViewModel::loadTasks,
                    onAddClick = taskEditViewModel::startCreatingTask,
                    onTaskClick = taskEditViewModel::loadTask,
                    onTaskLongClick = taskListViewModel::showDeleteConfirmation,
                    onDismissDelete = taskListViewModel::dismissDeleteConfirmation,
                    onConfirmDelete = taskListViewModel::deletePendingTask,
                    onTitleChange = taskEditViewModel::updateTitle,
                    onBodyChange = taskEditViewModel::updateBody,
                    onSaveClick = taskEditViewModel::saveTask
                )
            }
        }
    }
}
