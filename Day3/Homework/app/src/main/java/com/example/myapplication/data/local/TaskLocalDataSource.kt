package com.example.myapplication.data.local

import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {
    fun observeTasks(): Flow<List<TaskEntity>>
    suspend fun getTask(id: String): TaskEntity?
    suspend fun upsertTask(task: TaskEntity)
    suspend fun replaceSyncedTasks(tasks: List<TaskEntity>)
    suspend fun deleteTask(id: String)
}

class RoomTaskLocalDataSource(
    private val taskDao: TaskDao
) : TaskLocalDataSource {
    override fun observeTasks(): Flow<List<TaskEntity>> {
        return taskDao.observeTasks()
    }

    override suspend fun getTask(id: String): TaskEntity? {
        return taskDao.getTask(id)
    }

    override suspend fun upsertTask(task: TaskEntity) {
        taskDao.upsertTask(task)
    }

    override suspend fun replaceSyncedTasks(tasks: List<TaskEntity>) {
        taskDao.replaceSyncedTasks(tasks)
    }

    override suspend fun deleteTask(id: String) {
        taskDao.deleteTask(id)
    }
}
