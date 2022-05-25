package com.task.noteapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.RepositoryResource
import com.task.noteapp.utils.createTestNote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalNotesRepositoryShould {

    private lateinit var localNotesRepository: LocalNotesRepository

    @Mock
    private lateinit var notesDaoMock: NotesDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        localNotesRepository = LocalNotesRepository(notesDaoMock, UnconfinedTestDispatcher())
    }

    @Test
    fun `Add note using DAO`(): Unit = runTest {

        val note = createTestNote()

        localNotesRepository.addNewNote(note)

        verify(notesDaoMock).insertNote(note)
    }

    @Test
    fun `Update note using DAO`(): Unit = runTest {

        val note = createTestNote()

        localNotesRepository.updateNote(note)

        verify(notesDaoMock).updateNote(note)
    }


    @Test
    fun `Delete note using DAO`(): Unit = runTest {

        val noteId = "111"

        localNotesRepository.deleteNote(noteId)

        verify(notesDaoMock).deleteNoteById(noteId)
    }

    @Test
    fun `Fetch note by id using DAO`(): Unit = runTest {

        val noteId = "111"
        val note = createTestNote()
        whenever(notesDaoMock.getNoteById(noteId)) doReturn note

        val result = localNotesRepository.fetchNote(noteId)

        assertThat((result as RepositoryResource.Success).data).isEqualTo(note)

        verify(notesDaoMock).getNoteById(noteId)
    }

    @Test
    fun `Handle note fetching error`(): Unit = runTest {

        val noteId = "111"
        whenever(notesDaoMock.getNoteById(noteId)) doReturn null

        val result = localNotesRepository.fetchNote(noteId)

        assertThat(result).isInstanceOf(RepositoryResource.Error::class.java)

        verify(notesDaoMock).getNoteById(noteId)
    }

    @Test
    fun `Observe all notes`(): Unit = runTest {

        val data = MutableLiveData<List<Note>>()
        val notes = listOf(createTestNote(), createTestNote())
        whenever(notesDaoMock.getNotes()) doReturn data

        val notesLiveData = localNotesRepository.observeAllNotes()
        data.value = notes


        assertThat(notesLiveData.value).isEqualTo(notes)
        verify(notesDaoMock).getNotes()
    }
}