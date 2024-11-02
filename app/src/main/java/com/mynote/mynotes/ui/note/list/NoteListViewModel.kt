package com.mynote.mynotes.ui.note.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.db.toNoteOverview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class NoteListViewModel (private val noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteListViewModel"
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    private var notes:List<NoteOverview> = emptyList()
    var noteList by mutableStateOf(notes)

  /* init {
        viewModelScope.launch(Dispatchers.IO) { fetchLatestNotesInt() }
        } */

    fun fetchLatestNotes(){
        viewModelScope.launch(Dispatchers.IO) { fetchLatestNotesInt() }
    }

    fun deleteNote(id: String){
        Log.v(TAG, "deleting note ${id}")
        viewModelScope.launch(Dispatchers.IO) {
            val noteToDelete = noteRepository.get(id)
            if(null != noteToDelete){
                noteRepository.remove(noteToDelete)
                fetchLatestNotesInt()
            } else {
                Log.v(TAG, "Cannot delete. note $id not found")
            }
        }
    }

    fun fetchLatestNotesInt(){
        notes = noteRepository.getAll().map{n -> n.toNoteOverview()}
        Log.v(TAG, "fetched ${notes.size} notes");
        noteList = notes
        Log.v(TAG, "fetched note list ${noteList.size} notes");
    }
}