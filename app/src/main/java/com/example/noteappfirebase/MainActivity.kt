package com.example.noteappfirebase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvNotes: RecyclerView
    private lateinit var adapter: NoteAdapter
    private val noteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRV()
        noteViewModel.getNote().observe(this, {
            notes -> adapter.updateRV(notes)
        })

        binding.apply {
            btnSubmit.setOnClickListener {
                val noteContent = etName.text.toString()
                etName.text.clear()
                addNote(noteContent)
            }
        }
    }


    private fun setupRV() {
        rvNotes = binding.rvNotes
        adapter = NoteAdapter(this)
        rvNotes.adapter = adapter
        rvNotes.layoutManager = LinearLayoutManager(this)
    }

    private fun addNote(noteContent: String) {
        if (noteContent.isNotEmpty()) {
            noteViewModel.addNote(noteContent)
        }
    }

    private fun updateNote(noteID: String, newContent: String) {
        if (newContent.isNotEmpty()) {
            noteViewModel.editNote(noteID, newContent)
        }
    }

    private fun deleteNote(noteID: String) {
        noteViewModel.deleteNote(noteID)
    }

    fun showAlert(pk: String, content: String, type: String){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.setHint(content)
        if (type == "update") {
            dialogBuilder.setMessage("Update note")
                .setPositiveButton("Save") { _, _ ->
                    updateNote(pk, updatedNote.text.toString())
                }
        } else {
            dialogBuilder.setMessage("Are you sure to delete note?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteNote(pk)
                }
        }

        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, _ -> dialog.cancel()
        })

        val alert = dialogBuilder.create()
        if (type == "update") {
            alert.setTitle("Update")
            alert.setView(updatedNote)
        } else {
            alert.setTitle("delete")
        }
        alert.show()
    }
}