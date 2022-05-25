package com.task.noteapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.task.noteapp.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotesListFragment : Fragment() {

    @Inject
    lateinit var notesAdapter: NotesAdapter

    private val viewModel: NotesListViewModel by viewModels()

    private var _binding: FragmentNotesListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { newState -> handleNewState(newState) }
            }
        }

        binding.addNote.setOnClickListener { openNoteDetails() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleNewState(newState: NotesListViewModel.State) {
        with(binding) {

            if (newState.showInitialLoading) {
                loadingLayout.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
                notesList.visibility = View.GONE
            } else if (newState.showEmptyState) {
                loadingLayout.visibility = View.GONE
                emptyLayout.visibility = View.VISIBLE
                notesList.visibility = View.GONE
            } else {
                loadingLayout.visibility = View.GONE
                emptyLayout.visibility = View.GONE
                notesList.visibility = View.VISIBLE
                notesAdapter.submitList(newState.notes)
            }
        }
    }

    private fun openNoteDetails(noteId: String? = null) {
        val action = NotesListFragmentDirections.toDetails(noteId)
        findNavController().navigate(action)
    }

    private fun setUpAdapter() {
        binding.notesList.adapter = notesAdapter
        notesAdapter.setOnClickListener { note -> openNoteDetails(note.id) }
    }
}