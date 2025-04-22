package com.dotndash.mynotes.ui.note.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dotndash.mynotes.data.NoteOverview
import com.dotndash.mynotes.data.Tag
import com.dotndash.mynotes.db.NoteEntity
import com.dotndash.mynotes.db.NoteRepository
import com.dotndash.mynotes.db.toNoteOverview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class NoteListViewModel (private val noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteListViewModel"
    private var searchText = ""
    private var fromDate:Long = LocalDateTime.now().minusMonths(1L).toEpochSecond(ZoneOffset.UTC)
    private var toDate:Long = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)
    private var notes:List<NoteOverview> = emptyList()
    var noteList by mutableStateOf(notes)

    fun searchNotes(searchText: String?, fromDate: LocalDate?, toDate: LocalDate? ){
        Log.v(TAG, "Searching notes input  $searchText, fromDate $fromDate, toDate $toDate")
        this.searchText = if (!searchText.isNullOrBlank()) searchText else ""
        if(fromDate!=null){
            this.fromDate = fromDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
        }
        if(toDate!=null){
            this.toDate = toDate.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)
        }
        Log.v(TAG, "Searching notes with  ${this.searchText}, fromDate ${this.fromDate}, toDate ${this.toDate}")
        viewModelScope.launch(Dispatchers.IO) { fetchLatestNotesInt()}
    }

    fun deleteNote(id: String){
        Log.v(TAG, "deleting note $id")
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
        Log.v(TAG, "searching notes with title  %${searchText}% " +
                    "from date ${this.fromDate} to date ${this.toDate} ...")
        if(searchText.isNotBlank()) {
            notes = noteRepository
                .searchNotes("%${searchText}%",this.fromDate, this.toDate)
                .map{ n -> toNoteOverviews(n) }
        } else {
            notes = noteRepository
                .searchNotes(this.fromDate, this.toDate)
                .map{n -> toNoteOverviews(n)}
        }

        Log.v(TAG, "fetched ${notes.size} notes");
        noteList = notes
        Log.v(TAG, "fetched note list ${noteList.size} notes");
    }

    private fun toNoteOverviews(note : NoteEntity): NoteOverview {
        val tags = noteRepository.getTagsForNote(noteId = note.id)
            ?.map { t -> Tag(t.id,t.name) }
        return note.toNoteOverview(tags)
    }
}