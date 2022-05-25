package com.task.noteapp

import com.task.noteapp.data.model.Note
import java.util.*

fun createTestNote(title: String = "Title", description: String = "Description", imageUrl: String = "http://any.com/image.jpg") =
    Note(title, description, imageUrl, Date(), Date())
