package com.example.myapplication.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val noteRepository by lazy {
    NoteRepository()
}

class NoteRepository {
    private val notes = mutableListOf(
        Note(
            id = 1,
            title = "Kupovina",
            content = "Kruh, mlijeko, jaja",
            createdAt = "03.04.2026. 18:15"
        ),
        Note(
            id = 2,
            title = "Posao",
            content = "Zavrsiti zadatak do 18h",
            createdAt = "03.04.2026. 19:00"
        )
    )

    private val _notes = MutableStateFlow(notes.toList())
    val notesFlow = _notes.asStateFlow()

    fun getNote(noteId: Int): Note? = notes.firstOrNull { it.id == noteId }

    fun addNote(title: String, content: String): Note {
        val newNote = Note(
            id = (notes.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            content = content,
            createdAt = createTimestamp()
        )
        notes.add(newNote)
        publishNotes()
        return newNote
    }

    fun updateNote(noteId: Int, title: String, content: String): Note? {
        val noteIndex = notes.indexOfFirst { it.id == noteId }
        if (noteIndex == -1) {
            return null
        }

        val updatedNote = notes[noteIndex].copy(
            title = title,
            content = content
        )
        notes[noteIndex] = updatedNote
        publishNotes()
        return updatedNote
    }

    private fun publishNotes() {
        _notes.value = notes.toList()
    }

    private fun createTimestamp(): String {
        return SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault()).format(Date())
    }
}
