package com.task.noteapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.data.model.Note

@Dao
interface NotesDao {

    @Query("SELECT * from notes ORDER BY modified_time DESC")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE entryid = :noteId")
    suspend fun getNoteById(noteId: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note): Int

    @Query("DELETE FROM notes WHERE entryid = :noteId")
    suspend fun deleteNoteById(noteId: String): Int
}
