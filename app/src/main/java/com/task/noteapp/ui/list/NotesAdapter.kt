package com.task.noteapp.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.task.noteapp.R
import com.task.noteapp.data.model.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NotesAdapter @Inject constructor(@ApplicationContext private val appContext: Context) :
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


        val createdLabel =
            appContext.resources.getString(R.string.created, note.createdTime.formatAsString())
        holder.createdView.text = createdLabel

        if (note.createdTime != note.modifiedTime) {
            val updatedLabel = appContext.resources.getString(
                R.string.updated,
                note.modifiedTime.formatAsString()
            )
            holder.updatedView.text = updatedLabel
            holder.updatedView.visibility = View.VISIBLE
        } else {
            holder.updatedView.visibility = View.GONE
        }

        if (note.imageUrl.isNotBlank()) {
            Picasso.get().load(note.imageUrl).into(holder.noteImage)
            holder.noteImage.visibility = View.VISIBLE
        } else {
            holder.noteImage.visibility = View.GONE
        }
    }

    fun setOnClickListener(onClickHandler: (Note) -> Unit) {
        onClick = onClickHandler
    }

    class NotesAdapterHolder(view: View, private val onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val descriptionView: TextView = view.findViewById(R.id.description)
        val noteImage: ImageView = view.findViewById(R.id.note_image)
        val createdView: TextView = view.findViewById(R.id.note_created)
        val updatedView: TextView = view.findViewById(R.id.note_updated)

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


        private fun Date.formatAsString(): String {
            val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return simpleDateFormat.format(this)
        }

        private const val DATE_FORMAT = "dd-MM-yyyy"
    }
}

