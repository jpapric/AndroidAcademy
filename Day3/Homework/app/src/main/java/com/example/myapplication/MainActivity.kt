package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myapplication.navigation.NotesNavHost
import com.example.myapplication.presentation.NoteEditViewModel
import com.example.myapplication.presentation.NotesListViewModel
import com.example.myapplication.ui.theme.HomeworkTheme

class MainActivity : ComponentActivity() {
    private val notesListViewModel: NotesListViewModel by viewModels { NotesListViewModel.Factory }
    private val noteEditViewModel: NoteEditViewModel by viewModels { NoteEditViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeworkTheme {
                val listState by notesListViewModel.uiState.collectAsState()
                val editState by noteEditViewModel.uiState.collectAsState()

                NotesNavHost(
                    listState = listState,
                    editState = editState,
                    onAddClick = noteEditViewModel::startCreatingNote,
                    onNoteClick = noteEditViewModel::loadNote,
                    onTitleChange = noteEditViewModel::updateTitle,
                    onContentChange = noteEditViewModel::updateContent,
                    onSaveClick = noteEditViewModel::saveNote
                )
            }
        }
    }
}
