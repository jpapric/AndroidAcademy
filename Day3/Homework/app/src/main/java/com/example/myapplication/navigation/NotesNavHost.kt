package com.example.myapplication.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.model.Note
import com.example.myapplication.screens.NoteEditScreen
import com.example.myapplication.screens.NotesListScreen


@Composable
fun NotesNavHost() {
    val navController = rememberNavController()

    var notes by remember {
        mutableStateOf(
            listOf(
                Note(1, "Kupovina", "Kruh, mlijeko, jaja"),
                Note(2, "Posao", "Završiti zadatak do 18h")
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            NotesListScreen(
                notes = notes,
                onAdd = { navController.navigate(Screen.Edit.createRoute(-1)) },
                onNoteClick = { note ->
                    navController.navigate(Screen.Edit.createRoute(note.id))
                }
            )
        }

        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val noteToEdit = notes.firstOrNull { it.id == id }

            NoteEditScreen(
                note = noteToEdit,
                onSave = { newTitle, newDesc ->
                    notes =
                        if (noteToEdit == null) {
                            val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
                            notes + Note(newId, newTitle, newDesc)
                        } else {
                            notes.map {
                                if (it.id == noteToEdit.id) it.copy(
                                    title = newTitle,
                                    description = newDesc
                                ) else it
                            }
                        }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}