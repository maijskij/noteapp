package com.task.noteapp.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.utils.MainCoroutineRule
import com.task.noteapp.utils.createTestNote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotesListViewModelShould {

    private lateinit var notesListViewModel: NotesListViewModel

    @Mock
    private lateinit var notesRepositoryMock: NotesRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val observableNotes = MutableLiveData<List<Note>>()

    @Test
    fun `Set initial loading state while notes are loading`(): Unit = runTest {

        whenever(notesRepositoryMock.observeAllNotes()) doReturn observableNotes
        notesListViewModel = NotesListViewModel(notesRepositoryMock)


        val uiState = notesListViewModel.uiState.first()
        assertThat(uiState.notes).isEqualTo(emptyList<Note>())
        assertThat(uiState.showInitialLoading).isEqualTo(true)
    }

    @Test
    fun `Set state with empty notes list`(): Unit = runTest {

        whenever(notesRepositoryMock.observeAllNotes()) doReturn observableNotes

        notesListViewModel = NotesListViewModel(notesRepositoryMock)
        observableNotes.value = emptyList()

        val uiState = notesListViewModel.uiState.first()
        assertThat(uiState.notes).isEqualTo(emptyList<Note>())
        assertThat(uiState.showInitialLoading).isEqualTo(false)
    }

    @Test
    fun `Set state with loaded notes list`(): Unit = runTest {

        val newNotes = listOf(createTestNote("Title1"), createTestNote("Title2"))
        whenever(notesRepositoryMock.observeAllNotes()) doReturn observableNotes

        notesListViewModel = NotesListViewModel(notesRepositoryMock)
        observableNotes.value = newNotes

        val uiState = notesListViewModel.uiState.first()
        assertThat(uiState.notes).isEqualTo(newNotes)
        assertThat(uiState.showInitialLoading).isEqualTo(false)
    }
}

