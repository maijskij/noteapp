package com.task.noteapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.noteapp.util.createTestNote
import com.task.noteapp.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NotesDatabase

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAndGetNotesById(): Unit = runBlocking {
        val note1 = createTestNote("Title1")
        val note2 = createTestNote("Title2")

        database.notesDao().insertNote(note1)
        database.notesDao().insertNote(note2)

        assertThat(database.notesDao().getNoteById(note1.id)).isEqualTo(note1)
        assertThat(database.notesDao().getNoteById(note2.id)).isEqualTo(note2)
    }

    @Test
    fun insertAndDeleteNoteById(): Unit = runBlocking {
        val note1 = createTestNote()

        database.notesDao().insertNote(note1)
        assertThat(database.notesDao().getNoteById(note1.id)).isEqualTo(note1)

        database.notesDao().deleteNoteById(note1.id)
        assertThat(database.notesDao().getNoteById(note1.id)).isEqualTo(null)
    }

    @Test
    fun insertAndUpdateNote(): Unit = runBlocking {
        val note1 = createTestNote(description = "Old text")

        database.notesDao().insertNote(note1)
        note1.description = "New text"
        database.notesDao().updateNote(note1)

        assertThat(database.notesDao().getNoteById(note1.id)!!.description).isEqualTo("New text")
    }

    @Test
    fun insertAndObserveNotesInSortedByUpdateTimeInDescOrder(): Unit = runBlocking {
        val note1 = createTestNote("Title1", modified = Date(1))
        val note2 = createTestNote("Title2", modified = Date(2))

        database.notesDao().insertNote(note1)
        database.notesDao().insertNote(note2)

        val notesList = database.notesDao().getNotes().getOrAwaitValue()

        assertThat(notesList[0].title).isEqualTo(note2.title)
        assertThat(notesList[1].title).isEqualTo(note1.title)
    }
}