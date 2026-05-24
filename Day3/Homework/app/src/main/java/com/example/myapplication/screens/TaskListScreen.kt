package com.example.myapplication.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Task

@Composable
fun TaskListScreen(
    tasks: List<Task>,
    visibleTasks: List<Task>,
    completedTasks: Int,
    searchQuery: String,
    loading: Boolean,
    errorMessage: String?,
    taskPendingDelete: Task?,
    onAdd: () -> Unit,
    onRetry: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskLongClick: (Task) -> Unit,
    onTaskStatusClick: (Task) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit
) {

    Scaffold(
        containerColor = Color.Transparent,

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add task"
                )
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(padding)
        ) {

            when {

                loading -> {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null && tasks.isEmpty() -> {

                    ErrorContent(
                        message = errorMessage,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {

                    TaskListContent(
                        tasks = tasks,
                        visibleTasks = visibleTasks,
                        completedTasks = completedTasks,
                        searchQuery = searchQuery,
                        errorMessage = errorMessage,
                        onSearchQueryChange = onSearchQueryChange,
                        onTaskClick = onTaskClick,
                        onTaskLongClick = onTaskLongClick,
                        onTaskStatusClick = onTaskStatusClick
                    )
                }
            }
        }
    }

    if (taskPendingDelete != null) {

        AlertDialog(
            onDismissRequest = onDismissDelete,

            title = {
                Text("Delete task?")
            },

            text = {
                Text(taskPendingDelete.title)
            },

            confirmButton = {

                TextButton(
                    onClick = onConfirmDelete
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = onDismissDelete
                ) {
                    Text("Cancel")
                }
            },

            shape = RoundedCornerShape(24.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskListContent(
    tasks: List<Task>,
    visibleTasks: List<Task>,
    completedTasks: Int,
    searchQuery: String,
    errorMessage: String?,
    onSearchQueryChange: (String) -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskLongClick: (Task) -> Unit,
    onTaskStatusClick: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 18.dp, top = 22.dp, end = 18.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            TaskDashboardHeader(
                searchQuery = searchQuery,
                totalTasks = tasks.size,
                visibleTasks = visibleTasks.size,
                completedTasks = completedTasks,
                onSearchQueryChange = onSearchQueryChange
            )
        }

        if (errorMessage != null) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }

        when {
            tasks.isEmpty() -> item {
                EmptyState(
                    title = "No tasks yet",
                    body = "Create your first task and it will appear here."
                )
            }
            visibleTasks.isEmpty() -> item {
                EmptyState(
                    title = "No matching tasks",
                    body = "Try another title, body, active, or done keyword."
                )
            }
            else -> items(
                items = visibleTasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
                    task = task,
                    onTaskClick = onTaskClick,
                    onTaskLongClick = onTaskLongClick,
                    onTaskStatusClick = onTaskStatusClick
                )
            }
        }
    }
}

@Composable
private fun TaskDashboardHeader(
    searchQuery: String,
    totalTasks: Int,
    visibleTasks: Int,
    completedTasks: Int,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(26.dp),
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (totalTasks == 0) "Ready for your first task" else "Task list",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (totalTasks == 0) {
                        "Create a task and keep your day moving."
                    } else {
                        "$completedTasks completed, ${totalTasks - completedTasks} active"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SummaryPill(label = "Total", value = totalTasks.toString())
                    SummaryPill(label = "Shown", value = visibleTasks.toString())
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text("Search tasks...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),

                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun SummaryPill(
    label: String,
    value: String
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onTaskLongClick: (Task) -> Unit,
    onTaskStatusClick: (Task) -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier.combinedClickable(
            onClick = { onTaskClick(task) },
            onLongClick = { onTaskLongClick(task) }
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            StatusCircle(
                completed = task.isCompleted,
                onClick = { onTaskStatusClick(task) }
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(10.dp))
                    StatusPill(completed = task.isCompleted)
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = task.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatusCircle(
    completed: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(
                if (completed) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (completed) {
            Text(
                text = "✓",
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatusPill(completed: Boolean) {
    Surface(
        color = if (completed) {
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f)
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        contentColor = if (completed) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.primary
        },
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = if (completed) "Done" else "Active",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun EmptyState(
    title: String,
    body: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Retry")
        }
    }
}
