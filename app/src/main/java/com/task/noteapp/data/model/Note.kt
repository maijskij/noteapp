package com.task.noteapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "notes")
data class Note @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "created_time") var createdTime: Date,
    @ColumnInfo(name = "modified_time") var modifiedTime: Date,
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)