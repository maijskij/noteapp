package com.task.noteapp.domain

import androidx.lifecycle.LiveData
import com.task.noteapp.data.model.Note

interface NotesRepository {
    fun observeAllNotes(): LiveData<List<Note>>
    suspend fun fetchNote(noteId: String): RepositoryResource<Note>
    suspend fun addNewNote(newNote: Note)
    suspend fun updateNote(existingNote: Note)
    suspend fun deleteNote(toBeDeletedNoteId: String)
}