package com.task.noteapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource

class
FakeNotesRepository : NotesRepository {

    private val notesList = mutableListOf<Note>()

    private val observableNotes = MutableLiveData<List<Note>>()

    override fun observeAllNotes(): LiveData<List<Note>> {
        observableNotes.value = notesList
        return observableNotes
    }

    override suspend fun fetchNote(noteId: String): RepositoryResource<Note> {
        return RepositoryResource.Success(notesList[0])
    }

    override suspend fun addNewNote(newNote: Note) {
        notesList.add(newNote)
    }

    override suspend fun updateNote(existingNote: Note) {}

    override suspend fun deleteNote(toBeDeletedNoteId: String) {}
}

