package com.task.noteapp.noteslist

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.task.noteapp.mock.FakeNotesRepository
import com.task.noteapp.R
import com.task.noteapp.createTestNote
import com.task.noteapp.di.NotesRepositoryModule
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.ui.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@UninstallModules(NotesRepositoryModule::class)
@HiltAndroidTest
class NotesListTests {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val notesRepository: NotesRepository = FakeNotesRepository()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun showNotesFromTheRepository() {
        val title1 = "Title1"
        val description1 = "Description1"
        val title2 = "Title2"
        val description2 = "Description2"
        runBlocking { notesRepository.addNewNote(createTestNote(title1, description1)) }
        runBlocking { notesRepository.addNewNote(createTestNote(title2, description2)) }

        ActivityScenario.launch(MainActivity::class.java)

        onView(withText(title1)).check(matches(isDisplayed()))
        onView(withText(title2)).check(matches(isDisplayed()))
        onView(withText(description1)).check(matches(isDisplayed()))
        onView(withText(description2)).check(matches(isDisplayed()))
    }

    @Test
    fun openNewEmptyNoteWhenFloatingButtonPressed() {
        val title1 = "Title1"
        val description1 = "Description1"
        runBlocking { notesRepository.addNewNote(createTestNote(title1, description1)) }

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.add_note)).perform(click())

        onView(withId(R.id.note_title)).check(matches(isDisplayed()))
        onView(withId(R.id.note_title)).check(matches(withText("")))
        onView(withId(R.id.note_body)).check(matches(isDisplayed()))
        onView(withId(R.id.note_body)).check(matches(withText("")))
    }

    @Test
    fun openPrefilledNoteDetailsWhenNoteIsClicked() {
        val title1 = "Title1"
        val description1 = "Description1"
        runBlocking { notesRepository.addNewNote(createTestNote(title1, description1)) }

        ActivityScenario.launch(MainActivity::class.java)
        onView(withText(title1)).perform(click())

        onView(withId(R.id.note_title)).check(matches(isDisplayed()))
        onView(withId(R.id.note_title)).check(matches(withText(title1)))
        onView(withId(R.id.note_body)).check(matches(isDisplayed()))
        onView(withId(R.id.note_body)).check(matches(withText(description1)))
    }
}