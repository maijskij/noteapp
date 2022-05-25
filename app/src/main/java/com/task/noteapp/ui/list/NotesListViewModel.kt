package com.task.noteapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val allNotes: LiveData<List<Note>> = notesRepository.observeAllNotes()

    init {
        allNotes.observeForever { notes ->
            updateNotesList(notes)
        }
    }

    private fun updateNotesList(notes: List<Note>) {
        _uiState.update { currentState ->
            currentState.copy(
                notes = notes,
                showInitialLoading = false,
                showEmptyState = notes.isEmpty()
            )
        }
    }

    data class State(
        val showInitialLoading: Boolean = true,
        val showEmptyState: Boolean = false,
        val notes: List<Note> = emptyList()
    )
}