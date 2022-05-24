package com.task.noteapp

import com.task.noteapp.data.model.Note
import java.util.*

fun createTestNote(title: String, subTitle: String) = Note(title, subTitle, Date(), Date())
