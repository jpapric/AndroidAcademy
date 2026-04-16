package com.example.myapplication.presentation

data class NoteEditScreenState(
    val noteId: Int? = null,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val editingExistingNote: Boolean = false
)
