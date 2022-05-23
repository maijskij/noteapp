package com.task.noteapp.domain

interface NotesRepository {
    suspend fun fetchAllNotes(): RepositoryResource<List<Note>>
    suspend fun fetchNote(noteId: String): RepositoryResource<Note>
    suspend fun addNewNote(newNote: Note)
    suspend fun updateNote(existingNote: Note)
    suspend fun deleteNote(toBeDeletedNoteId: String)
}