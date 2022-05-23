package com.task.noteapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.R
import com.task.noteapp.domain.Note
import javax.inject.Inject

class NotesAdapter @Inject constructor() :
    ListAdapter<Note, NotesAdapter.NotesAdapterHolder>(DIFF_CALLBACK) {

    private var onClick: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapterHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_row_item, parent, false)

        return NotesAdapterHolder(view) { position -> onClick?.invoke(getItem(position)) }
    }

    override fun onBindViewHolder(holder: NotesAdapterHolder, position: Int) {
        val note = getItem(position)
        holder.titleView.text = note.title
        holder.descriptionView.text = note.description
    }

    fun setOnClickListener(onClickHandler: (Note) -> Unit) {
        onClick = onClickHandler
    }

    class NotesAdapterHolder(view: View, private val onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val descriptionView: TextView = view.findViewById(R.id.description)

        init {
            view.setOnClickListener { onClick(adapterPosition) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                oldItem == newItem
        }
    }
}

