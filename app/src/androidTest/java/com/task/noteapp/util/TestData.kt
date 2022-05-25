package com.task.noteapp.util

import com.task.noteapp.data.model.Note
import java.util.*

fun createTestNote(
    title: String = "Title",
    description: String = "Description",
    imageUrl: String = "http://any.com/image.jpg",
    created: Date = Date(),
    modified: Date = Date()
) = Note(title, description, imageUrl, created, modified)
