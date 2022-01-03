package com.example.noteappfirebase
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappfirebase.databinding.RowNoteBinding

class NoteAdapter(private val activity: MainActivity): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var notes = listOf<Note>()
    class NoteViewHolder(val binding: RowNoteBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(RowNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            tvTitle.text = note.content

            ivUpdate.setOnClickListener { activity.showAlert(note.pk, note.content, "update") }
            ivDelete.setOnClickListener {  activity.showAlert(note.pk, note.content, "delete")}
        }
    }

    override fun getItemCount() = notes.size

    fun updateRV(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}