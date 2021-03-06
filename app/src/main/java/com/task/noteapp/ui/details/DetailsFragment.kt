package com.task.noteapp.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentNoteDetailsBinding
import com.task.noteapp.ui.utils.bidirectionalBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentNoteDetailsBinding? = null

    private val viewModel: DetailsViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { newState -> handleNewState(newState) }
            }
        }

        binding.noteAction.setOnClickListener { viewModel.saveOrUpdate() }

        binding.noteBody.bidirectionalBinding(viewLifecycleOwner, viewModel.noteBodyText)
        binding.noteTitle.bidirectionalBinding(viewLifecycleOwner, viewModel.noteTitleText)
        binding.noteImageUrl.bidirectionalBinding(viewLifecycleOwner, viewModel.noteImageUrl)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteNote()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleNewState(newState: DetailsViewModel.State) {
        if (newState.errorMessageResId != null) {
            Snackbar.make(binding.root, newState.errorMessageResId, Snackbar.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
        if (newState.goToNotesList) {
            findNavController().navigateUp()
        }
    }
}