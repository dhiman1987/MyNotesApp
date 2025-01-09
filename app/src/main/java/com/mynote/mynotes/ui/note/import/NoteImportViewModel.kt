package com.mynote.mynotes.ui.note.import

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.db.NoteRepository
import java.time.format.DateTimeFormatter

class NoteImportViewModel (private val noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteImportViewModel"
   var selectedNotes = mutableStateListOf<NoteOverview>()
        private set
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")

    fun deleteSelectedNote(id:String){
        selectedNotes.removeAll { note -> note.id == id }
    }

    fun updateSelectedNotes(updatedNotes:List<NoteOverview>) {
        selectedNotes.clear()
        updatedNotes.forEach{ note -> selectedNotes.add(note)}
    }

}