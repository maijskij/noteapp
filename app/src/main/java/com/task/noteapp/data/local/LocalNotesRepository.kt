package com.task.noteapp.data.local

import androidx.lifecycle.LiveData
import com.task.noteapp.data.model.Note
import com.task.noteapp.di.CoroutineDispatcherIO
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocalNotesRepository @Inject constructor(
    private val notesDao: NotesDao,
    @CoroutineDispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : NotesRepository {

    override fun observeAllNotes(): LiveData<List<Note>> = notesDao.getNotes()

    override suspend fun fetchNote(noteId: String): RepositoryResource<Note> =
        withContext(dispatcherIO) {
            try {
                val note = notesDao.getNoteById(noteId)
                if (note != null) {
                    RepositoryResource.Success(note)
                } else {
                    RepositoryResource.Error(Throwable("Note not found!"))
                }
            } catch (e: Exception) {
                RepositoryResource.Error(e)
            }
        }

    override suspend fun addNewNote(newNote: Note) = withContext(dispatcherIO) {
        notesDao.insertNote(newNote)
    }

    override suspend fun updateNote(existingNote: Note) = withContext<Unit>(dispatcherIO) {
        notesDao.updateNote(existingNote)
    }

    override suspend fun deleteNote(toBeDeletedNoteId: String) = withContext<Unit>(dispatcherIO) {
        notesDao.deleteNoteById(toBeDeletedNoteId)
    }
}