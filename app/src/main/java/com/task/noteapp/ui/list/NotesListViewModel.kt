package com.task.noteapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.domain.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            when (val result = notesRepository.fetchAllNotes()) {
                is RepositoryResource.Error -> {
                    updateErrorMessage(result.error.toString())
                    updateNotesList(emptyList())
                }
                is RepositoryResource.Success -> {
                    updateNotesList(result.data)
                }
            }
            disableInitialLoading()
        }
    }

    fun onErrorShown() {
        updateErrorMessage(null)
    }

    private fun disableInitialLoading() {
        _uiState.update { currentState ->
            currentState.copy(showInitialLoading = false)
        }
    }

    private fun updateErrorMessage(errorMessage: String?) {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = errorMessage)
        }
    }

    private fun updateNotesList(notes: List<Note>) {
        _uiState.update { currentState ->
            currentState.copy(notes = notes)
        }
    }

    data class State(
        val errorMessage: String? = null,
        val showInitialLoading: Boolean = true,
        val notes: List<Note> = emptyList()
    )
}