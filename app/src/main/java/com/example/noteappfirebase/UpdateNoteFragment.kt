package com.example.noteappfirebase

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noteappfirebase.databinding.FragmentUpdateNoteBinding

class UpdateNoteFragment : Fragment() {

    private lateinit var binding: FragmentUpdateNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateNoteBinding.inflate(layoutInflater, container, false)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("Notes", Context.MODE_PRIVATE)

        binding.apply {
            btnUpdate.setOnClickListener {
                val noteContentUpdated = etUpdatedNote.text.toString()
                etUpdatedNote.text.clear()
                val pk = sharedPreferences.getString("NoteID","")!!
                updateNote(pk, noteContentUpdated)
                Navigation.findNavController(binding.root).navigate(R.id.action_updateNoteFragment_to_noteFragment)
            }
        }
        return binding.root
    }

    private fun updateNote(noteID: String, newContent: String) {
        if (newContent.isNotEmpty()) {
            noteViewModel.editNote(noteID, newContent)
        }
    }


}