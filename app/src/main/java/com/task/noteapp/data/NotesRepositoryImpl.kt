package com.task.noteapp.data

import com.task.noteapp.domain.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(): NotesRepository {
    override suspend fun fetchAllNotes(): RepositoryResource<List<Note>> {
        return RepositoryResource.Success(
            listOf(
                Note("1", "Title 1", "Description 1"),
                Note("2", "Title 2", "Description 2")
            )
        )
    }

    override suspend fun fetchNote(noteId: String): RepositoryResource<Note> {
        return RepositoryResource.Success(Note("1", "Title 1", "Description 1"))
    }

    override suspend fun addNewNote(newNote: Note) {

    }

    override suspend fun updateNote(existingNote: Note) {

    }

    override suspend fun deleteNote(toBeDeletedNoteId: String) {

    }
}