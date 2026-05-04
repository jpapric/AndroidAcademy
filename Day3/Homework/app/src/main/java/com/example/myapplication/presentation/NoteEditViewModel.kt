package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.model.NoteRepository
import com.example.myapplication.model.noteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteEditViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteEditScreenState())
    val uiState = _uiState.asStateFlow()

    fun startCreatingNote() {
        _uiState.value = NoteEditScreenState()
    }

    fun loadNote(noteId: Int) {
        val note = noteRepository.getNote(noteId)
        _uiState.value = if (note == null) {
            NoteEditScreenState()
        } else {
            NoteEditScreenState(
                noteId = note.id,
                title = note.title,
                content = note.content,
                createdAt = note.createdAt,
                editingExistingNote = true
            )
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun saveNote() {
        val currentState = _uiState.value
        val title = currentState.title.trim()
        val content = currentState.content.trim()

        if (currentState.editingExistingNote) {
            val noteId = currentState.noteId ?: return
            val updatedNote = noteRepository.updateNote(
                noteId = noteId,
                title = title,
                content = content
            ) ?: return

            _uiState.value = currentState.copy(
                title = updatedNote.title,
                content = updatedNote.content,
                createdAt = updatedNote.createdAt
            )
            return
        }

        val newNote = noteRepository.addNote(
            title = title,
            content = content
        )
        _uiState.value = NoteEditScreenState(
            noteId = newNote.id,
            title = newNote.title,
            content = newNote.content,
            createdAt = newNote.createdAt,
            editingExistingNote = true
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                NoteEditViewModel(noteRepository) as T
        }
    }
}
