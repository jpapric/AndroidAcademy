package com.example.myapplication.presentation

import com.example.myapplication.model.Task

data class TaskListScreenState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val taskPendingDelete: Task? = null
) {
    val visibleTasks: List<Task>
        get() {
            val query = searchQuery.trim()
            if (query.isEmpty()) {
                return tasks
            }

            return tasks.filter { task ->
                task.title.contains(query, ignoreCase = true) ||
                    task.body.contains(query, ignoreCase = true) ||
                    statusLabel(task).contains(query, ignoreCase = true)
            }
        }

    val completedTasks: Int
        get() = tasks.count { it.isCompleted }
}

private fun statusLabel(task: Task): String {
    return if (task.isCompleted) "done completed" else "active open"
}
