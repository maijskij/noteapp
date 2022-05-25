package com.task.noteapp.ui.details

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.task.noteapp.R
import com.task.noteapp.di.NotesRepositoryModule
import com.task.noteapp.domain.NotesRepository
import com.task.noteapp.mock.FakeNotesRepository
import com.task.noteapp.ui.launchMainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@UninstallModules(NotesRepositoryModule::class)
@HiltAndroidTest
class NoteDetailsTests {

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
    fun notesWithoutTitleIsNotSaved() {

        launchMainActivity()
        onView(withId(R.id.add_note)).perform(click())

        onView(withId(R.id.note_action)).perform(click())
        // Screen is not closed, details screen form is till present
        onView(withId(R.id.note_title)).check(matches(isDisplayed()))
    }

    @Test
    fun notesWithTitleSaved() {

        launchMainActivity()

        onView(withId(R.id.add_note)).perform(click())
        onView(withId(R.id.note_title)).perform(replaceText("Not empty title"))

        onView(withId(R.id.note_action)).perform(click())
        // Screen is closed, details screen form is not present
        onView(withId(R.id.note_title)).check(doesNotExist())
    }
}