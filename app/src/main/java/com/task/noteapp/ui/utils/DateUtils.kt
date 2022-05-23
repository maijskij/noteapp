package com.task.noteapp.ui.utils

import android.text.format.DateUtils
import java.util.*

fun Date.asHumanReadableString(): String {
    val timeNow = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(time, timeNow, DateUtils.SECOND_IN_MILLIS).toString()
}