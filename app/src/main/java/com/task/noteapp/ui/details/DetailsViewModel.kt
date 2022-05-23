package com.task.noteapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    var noteBodyText = MutableLiveData("")
    var noteTitleText = MutableLiveData("")

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val noteId = savedStateHandle.get<String>("idAsString")

    init {
        if (noteId != null) {
            loadExistingNote(noteId)
        }
    }

    fun onErrorShown() {
        updateErrorMessage(null)
    }

    fun deleteNote() {
        viewModelScope.launch {
            if (noteId != null) {
                notesRepository.deleteNote(noteId)
            }
            goToNotesList()
        }
    }

    fun saveOrUpdate() {
        viewModelScope.launch {
            if (noteId == null) {
                notesRepository.addNewNote(createNote())
            } else {
                notesRepository.updateNote(createNote())
            }
        }
    }

    private fun loadExistingNote(noteId: String) {
        viewModelScope.launch {
            when (val result = notesRepository.fetchNote(noteId)) {
                is RepositoryResource.Error -> {
                    updateErrorMessage(result.error.toString())
                }
                is RepositoryResource.Success -> {
                    noteTitleText.value = result.data.title
                    noteBodyText.value = result.data.description
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: String?) {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = errorMessage)
        }
    }


    private fun goToNotesList() {
        _uiState.update { currentState ->
            currentState.copy(goToNotesList = true)
        }
    }

    private fun createNote() =
        if (noteId == null) {
            Note("1", noteTitleText.toString(), noteBodyText.toString())
        } else {
            Note(noteId, noteTitleText.toString(), noteBodyText.toString())
        }


    data class State(
        val errorMessage: String? = null,
        val goToNotesList: Boolean = false
    )
}