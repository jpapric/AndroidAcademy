package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.NoteEditScreenState
import com.example.myapplication.presentation.NotesListScreenState
import com.example.myapplication.screens.NoteEditScreen
import com.example.myapplication.screens.NotesListScreen

@Composable
fun NotesNavHost(
    listState: NotesListScreenState,
    editState: NoteEditScreenState,
    onAddClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            NotesListScreen(
                notes = listState.notes,
                onAdd = {
                    onAddClick()
                    navController.navigate(Screen.Edit.route)
                },
                onNoteClick = { note ->
                    onNoteClick(note.id)
                    navController.navigate(Screen.Edit.route)
                }
            )
        }

        composable(Screen.Edit.route) {
            NoteEditScreen(
                state = editState,
                onTitleChange = onTitleChange,
                onContentChange = onContentChange,
                onSave = {
                    onSaveClick()
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
