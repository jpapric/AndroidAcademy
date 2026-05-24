package com.example.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.presentation.TaskEditScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    state: TaskEditScreenState,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.editingExistingTask) {
                            "Edit Task"
                        } else {
                            "Create Task"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },

                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.loading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                return@Column
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

                color = MaterialTheme.colorScheme.surface,

                tonalElevation = 2.dp,

                shadowElevation = 2.dp
            ) {

                Column(
                    modifier = Modifier.padding(22.dp),

                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = if (state.editingExistingTask) {
                            "Update your task"
                        } else {
                            "Create a new task"
                        },

                        style = MaterialTheme.typography.headlineSmall,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Keep the title clear and details actionable.",

                        style = MaterialTheme.typography.bodyMedium,

                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = state.title,

                        onValueChange = onTitleChange,

                        label = {
                            Text("Task Title")
                        },

                        placeholder = {
                            Text("Example: Finish UI redesign")
                        },

                        modifier = Modifier.fillMaxWidth(),

                        enabled = !state.saving,

                        singleLine = true,

                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.body,

                        onValueChange = onBodyChange,

                        label = {
                            Text("Task Details")
                        },

                        placeholder = {
                            Text(
                                "Describe what needs to be done, next steps..."
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 170.dp),

                        enabled = !state.saving,

                        shape = RoundedCornerShape(20.dp)
                    )

                    state.errorMessage?.let { error ->

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = error,

                            style = MaterialTheme.typography.bodyMedium,

                            color = MaterialTheme.colorScheme.error,

                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = onSave,

                        enabled = !state.saving &&
                                (!state.editingExistingTask || state.canUpdateRemoteTask),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Text(
                            text = if (state.saving) {
                                "Saving..."
                            } else {
                                "Save Task"
                            },

                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}