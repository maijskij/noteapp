package com.task.noteapp.utils

import com.task.noteapp.data.model.Note
import java.util.*


fun createTestNote(title: String = "Title", subTitle: String = "Description") =
    Note(title, subTitle, Date(), Date())
