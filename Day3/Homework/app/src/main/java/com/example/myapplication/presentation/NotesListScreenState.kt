package com.example.myapplication.presentation

import com.example.myapplication.model.Note

data class NotesListScreenState(
    val notes: List<Note> = emptyList()
)
