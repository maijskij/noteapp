package com.task.noteapp

import com.task.noteapp.data.model.Note
import java.util.*

fun createTestNote(title: String, subTitle: String, imageUrl: String = "http://any.com/image.jpg") =
    Note(title, subTitle, imageUrl, Date(), Date())
