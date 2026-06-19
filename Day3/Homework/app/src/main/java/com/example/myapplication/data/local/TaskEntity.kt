package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val username: String,
    val isRemoteEditable: Boolean,
    val isCompleted: Boolean,
    val isPendingSync: Boolean,
    val updatedAt: Long
)
