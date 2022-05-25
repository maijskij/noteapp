package com.task.noteapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.R
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    var noteBodyText = MutableLiveData("")
    var noteTitleText = MutableLiveData("")
    var noteImageUrl = MutableLiveData("")

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private var existingNote: Note? = null

    init {
        val noteId = savedStateHandle.get<String>("idAsString")
        if (noteId != null) {
            loadExistingNote(noteId)
        }
    }

    fun onErrorShown() {
        updateErrorMessage(null)
    }

    fun deleteNote() {
        viewModelScope.launch {
            existingNote?.let {
                notesRepository.deleteNote(it.id)
            }
            goToNotesList()
        }
    }

    fun saveOrUpdate() {
        if (fieldsAreNotValid()) {
            updateErrorMessage(R.string.title_validation_failed)
            return
        }

        viewModelScope.launch {
            val note = assembleNote()

            if (existingNote == null) {
                notesRepository.addNewNote(note)
            } else {
                notesRepository.updateNote(note)
            }
            goToNotesList()
        }
    }

    private fun fieldsAreNotValid() = noteTitleText.value.isNullOrBlank()

    private fun loadExistingNote(noteId: String) {
        viewModelScope.launch {
            when (val result = notesRepository.fetchNote(noteId)) {
                is RepositoryResource.Error -> {
                    updateErrorMessage(R.string.note_loading_error)
                }
                is RepositoryResource.Success -> {
                    existingNote = result.data
                    noteTitleText.value = result.data.title
                    noteBodyText.value = result.data.description
                    noteImageUrl.value = result.data.imageUrl
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessageResId: Int?) {
        _uiState.update { currentState ->
            currentState.copy(errorMessageResId = errorMessageResId)
        }
    }

    private fun goToNotesList() {
        _uiState.update { currentState ->
            currentState.copy(goToNotesList = true)
        }
    }

    private fun assembleNote(): Note {
        val note = existingNote
        val timeNow = Date(System.currentTimeMillis())
        return note?.copy(
            title = noteTitleText.value.toString(),
            description = noteBodyText.value.toString(),
            imageUrl = noteImageUrl.value.toString(),
            modifiedTime = timeNow
        ) ?: Note(
            noteTitleText.value.toString(),
            noteBodyText.value.toString(),
            noteImageUrl.value.toString(),
            timeNow,
            timeNow
        )
    }


    data class State(
        val errorMessageResId: Int? = null,
        val goToNotesList: Boolean = false
    )
}