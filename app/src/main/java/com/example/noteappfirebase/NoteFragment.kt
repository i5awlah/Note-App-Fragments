package com.example.noteappfirebase

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappfirebase.databinding.ActivityMainBinding
import com.example.noteappfirebase.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {
    private lateinit var rvNotes: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: FragmentNoteBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Notes", Context.MODE_PRIVATE)
        setupRV()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNote().observe(viewLifecycleOwner, {
                notes -> adapter.updateRV(notes)
        })

        binding.apply {
            btnSubmit.setOnClickListener {
                val noteContent = etName.text.toString()
                etName.text.clear()
                addNote(noteContent)
            }
        }

        return binding.root
    }

    private fun setupRV() {
        rvNotes = binding.rvNotes
        adapter = NoteAdapter(this)
        rvNotes.adapter = adapter
        rvNotes.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun addNote(noteContent: String) {
        if (noteContent.isNotEmpty()) {
            noteViewModel.addNote(noteContent)
        }
    }



    private fun deleteNote(noteID: String) {
        noteViewModel.deleteNote(noteID)
    }

    fun goUpdate() {
        Navigation.findNavController(binding.root).navigate(R.id.action_noteFragment_to_updateNoteFragment)
    }

    fun showAlert(pk: String){
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setMessage("Are you sure to delete note?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteNote(pk)
                }
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, _ -> dialog.cancel()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("delete")
        alert.show()
    }

}