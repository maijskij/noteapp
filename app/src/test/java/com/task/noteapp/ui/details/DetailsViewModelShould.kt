package com.task.noteapp.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.task.noteapp.data.model.Note
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.domain.RepositoryResource
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
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelShould {

    private lateinit var detailsViewModel: DetailsViewModel

    @Mock
    private lateinit var notesRepositoryMock: NotesRepository

    @Mock
    private lateinit var savedStateHandleMock: SavedStateHandle

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Set initial state for the new note`(): Unit = runTest {

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)

        verify(notesRepositoryMock, never()).fetchNote(anyOrNull())
        val uiState = detailsViewModel.uiState.first()
        assertThat(uiState.errorMessage).isEqualTo(null)
        assertThat(uiState.goToNotesList).isEqualTo(false)

    }

    @Test
    fun `Load note using noteId from savedStateHandle`(): Unit = runTest {

        val note = createTestNote()
        val noteId = "1"
        whenever(savedStateHandleMock.get<String>("idAsString")) doReturn noteId
        whenever(notesRepositoryMock.fetchNote(noteId)) doReturn RepositoryResource.Success(note)

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)

        verify(notesRepositoryMock).fetchNote(noteId)
        val uiState = detailsViewModel.uiState.first()
        assertThat(uiState.errorMessage).isEqualTo(null)
        assertThat(uiState.goToNotesList).isEqualTo(false)
        assertThat(detailsViewModel.noteTitleText.value).isEqualTo(note.title)
        assertThat(detailsViewModel.noteBodyText.value).isEqualTo(note.description)
    }

    @Test
    fun `Set error state when note loading failed`(): Unit = runTest {

        val errorMessage = "Something went wrong!"
        val throwable = Throwable(errorMessage)
        val noteId = "1"
        whenever(savedStateHandleMock.get<String>("idAsString")) doReturn noteId
        whenever(notesRepositoryMock.fetchNote(noteId)) doReturn RepositoryResource.Error(throwable)

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)

        val uiState = detailsViewModel.uiState.first()
        assertThat(uiState.errorMessage).contains(errorMessage)
        assertThat(uiState.goToNotesList).isEqualTo(false)
        assertThat(detailsViewModel.noteTitleText.value).isEqualTo("")
        assertThat(detailsViewModel.noteBodyText.value).isEqualTo("")
    }

    @Test
    fun `Clear error state when error has been shown to the user`(): Unit = runTest {

        val errorMessage = "Something went wrong!"
        val throwable = Throwable(errorMessage)
        val noteId = "1"
        whenever(savedStateHandleMock.get<String>("idAsString")) doReturn noteId
        whenever(notesRepositoryMock.fetchNote(noteId)) doReturn RepositoryResource.Error(throwable)

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)
        detailsViewModel.onErrorShown()

        assertThat(detailsViewModel.uiState.first().errorMessage).isEqualTo(null)
    }


    @Test
    fun `Delete note when option menu button pressed`(): Unit = runTest {

        val note = createTestNote()
        val noteId = "1"
        whenever(savedStateHandleMock.get<String>("idAsString")) doReturn noteId
        whenever(notesRepositoryMock.fetchNote(noteId)) doReturn RepositoryResource.Success(note)

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)
        detailsViewModel.deleteNote()

        verify(notesRepositoryMock).deleteNote(note.id)
        val uiState = detailsViewModel.uiState.first()
        assertThat(uiState.errorMessage).isEqualTo(null)
        assertThat(uiState.goToNotesList).isEqualTo(true)
    }

    @Test
    fun `Save note when floating button pressed`(): Unit = runTest {
        val title = "Title"
        val description = "Description"
        val imageUrl = "https://image"

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)
        detailsViewModel.noteTitleText.value = title
        detailsViewModel.noteBodyText.value = description
        detailsViewModel.noteImageUrl.value = imageUrl
        detailsViewModel.saveOrUpdate()

        val noteCaptor = argumentCaptor<Note>()
        verify(notesRepositoryMock).addNewNote(noteCaptor.capture())
        assertThat(noteCaptor.firstValue.title).isEqualTo(title)
        assertThat(noteCaptor.firstValue.description).isEqualTo(description)
        assertThat(noteCaptor.firstValue.imageUrl).isEqualTo(imageUrl)
        assertThat(noteCaptor.firstValue.modifiedTime).isEqualTo(noteCaptor.firstValue.createdTime)
        assertThat( detailsViewModel.uiState.first().goToNotesList).isEqualTo(true)
    }

    @Test
    fun `Update note when floating button pressed`(): Unit = runTest {
        val title = "Title2"
        val description = "Description2"
        val imageUrl = "https://image2"
        val note = createTestNote()
        val noteId = "1"
        whenever(savedStateHandleMock.get<String>("idAsString")) doReturn noteId
        whenever(notesRepositoryMock.fetchNote(noteId)) doReturn RepositoryResource.Success(note)

        detailsViewModel = DetailsViewModel(savedStateHandleMock, notesRepositoryMock)
        detailsViewModel.noteTitleText.value = title
        detailsViewModel.noteBodyText.value = description
        detailsViewModel.noteImageUrl.value = imageUrl
        detailsViewModel.saveOrUpdate()

        val noteCaptor = argumentCaptor<Note>()
        verify(notesRepositoryMock).updateNote(noteCaptor.capture())
        assertThat(noteCaptor.firstValue.title).isEqualTo(title)
        assertThat(noteCaptor.firstValue.description).isEqualTo(description)
        assertThat(noteCaptor.firstValue.imageUrl).isEqualTo(imageUrl)
        assertThat(noteCaptor.firstValue.modifiedTime).isAfter(noteCaptor.firstValue.createdTime)
        assertThat( detailsViewModel.uiState.first().goToNotesList).isEqualTo(true)
    }
}