package com.example.myapplication.di

import com.example.myapplication.data.NetworkModule
import com.example.myapplication.data.TaskRemoteDataSource
import com.example.myapplication.model.TaskRepository
import com.example.myapplication.util.Logger

class AppContainer {
    private val networkLogger = Logger("Network")
    private val api = NetworkModule.createTaskieApi(networkLogger)

    private val taskRemoteDataSource = TaskRemoteDataSource(
        api = api,
        logger = Logger("TaskRemoteDataSource")
    )

    val taskRepository = TaskRepository(
        remoteDataSource = taskRemoteDataSource,
        logger = Logger("TaskRepository")
    )

    val loginViewModelLogger = Logger("LoginViewModel")
    val taskListViewModelLogger = Logger("TaskListViewModel")
    val taskEditViewModelLogger = Logger("TaskEditViewModel")
}
