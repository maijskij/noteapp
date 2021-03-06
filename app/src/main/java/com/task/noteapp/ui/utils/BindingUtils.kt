package com.task.noteapp.ui.utils

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

fun EditText.bidirectionalBinding(
    lifecycleOwner: LifecycleOwner,
    liveData: MutableLiveData<String>
) {

    liveData.observe(lifecycleOwner) {
        if (text.toString() != it) {
            setText(it.toString())
        }
    }

    doOnTextChanged { text, _, _, _ ->
        val newValue = text?.toString()
        if (liveData.value != newValue) {
            liveData.value = newValue
        }
    }
}
