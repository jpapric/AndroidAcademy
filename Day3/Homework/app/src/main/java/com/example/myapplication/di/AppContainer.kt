package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.NetworkModule
import com.example.myapplication.data.TaskRemoteDataSource
import com.example.myapplication.data.local.RoomTaskLocalDataSource
import com.example.myapplication.data.local.TaskDatabase
import com.example.myapplication.model.TaskRepository
import com.example.myapplication.util.Logger

class AppContainer(context: Context) {
    private val networkLogger = Logger("Network")
    private val database = TaskDatabase.create(context)
    private val taskLocalDataSource = RoomTaskLocalDataSource(database.taskDao())

    private val taskRemoteDataSource = TaskRemoteDataSource(
        apiProvider = { NetworkModule.createTaskieApi(networkLogger) },
        logger = Logger("TaskRemoteDataSource")
    )

    val taskRepository = TaskRepository(
        remoteDataSource = taskRemoteDataSource,
        localDataSource = taskLocalDataSource,
        logger = Logger("TaskRepository")
    )

    val loginViewModelLogger = Logger("LoginViewModel")
    val taskListViewModelLogger = Logger("TaskListViewModel")
    val taskEditViewModelLogger = Logger("TaskEditViewModel")
}
