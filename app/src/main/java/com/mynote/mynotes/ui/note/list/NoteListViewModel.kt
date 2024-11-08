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

class NoteListViewModel (private val noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteListViewModel"
    private var searchText = ""
    private var notes:List<NoteOverview> = emptyList()
    var noteList by mutableStateOf(notes)


    fun fetchLatestNotes(searchText: String?){
        if(!searchText.isNullOrBlank()){
            this.searchText = searchText
        } else {
            this.searchText = ""
        }
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

    private fun fetchLatestNotesInt(){
        Log.v(TAG, "searchText $searchText notes");
        if(searchText.isNotBlank()){
            Log.v(TAG, "searching notes with title  %${searchText}% ...")
            notes = noteRepository.getAll("%${searchText}%").map{n -> n.toNoteOverview()}

        } else {
            Log.v(TAG, "searching all notes")
            notes = noteRepository.getAll().map{n -> n.toNoteOverview()}
        }
        Log.v(TAG, "fetched ${notes.size} notes");
        noteList = notes
        Log.v(TAG, "fetched note list ${noteList.size} notes");
    }
}