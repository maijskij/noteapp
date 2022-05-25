package com.task.noteapp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import com.task.noteapp.R

fun launchMainActivity(): ActivityScenario<MainActivity>? {
    val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    activityScenario.onActivity { activity ->
        // Disable animations in RecyclerView
        (activity.findViewById(R.id.notesList) as RecyclerView).itemAnimator = null
    }
    return activityScenario
}