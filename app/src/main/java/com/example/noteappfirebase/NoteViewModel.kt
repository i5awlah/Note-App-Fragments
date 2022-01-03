package com.example.noteappfirebase


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteViewModel: ViewModel() {
    private val db = Firebase.firestore
    private var notes: MutableLiveData<List<Note>> = MutableLiveData()
    private val TAG = "Main"

    init {
        getData()
    }

    private fun getData() {
        val tempNotes = arrayListOf<Note>()
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    document.data.map { (key, value)
                        ->
                        tempNotes.add(Note(document.id, value.toString()))
                    }
                }
                notes.postValue(tempNotes)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getNote(): LiveData<List<Note>> {
        return notes
    }

    fun addNote(noteContent: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val newNote = hashMapOf(
                "content" to noteContent
            )
            // Add a new document with a generated ID
            db.collection("notes")
                .add(newNote)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    getData()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun editNote(noteID: String, noteContent: String){
        CoroutineScope(Dispatchers.IO).launch {
            val updatedNote = hashMapOf(
                "content" to noteContent
            )

            db.collection("notes")
                .document(noteID).update(updatedNote as Map<String, Any>)
            getData()
        }
    }

    fun deleteNote(noteID: String){
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("notes")
                .document(noteID).delete()
            getData()
        }
    }

}