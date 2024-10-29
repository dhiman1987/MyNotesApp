package com.mynote.mynotes.ui.note.editor

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class NoteEditorViewModel(noteId: String, noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteEditorViewModel"
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    private var noteModel: NoteModel = NoteModel("",noteRepository)

    var noteContent by mutableStateOf(noteModel.get().content)
    var noteTitle by mutableStateOf(noteModel.get().title)
    var noteUpdatedOn by mutableStateOf<String>(noteModel.get().updatedOn.format(formatter))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v(TAG, "fetching note $noteId");
            noteModel = NoteModel(noteId,noteRepository);
            Log.v(TAG, "fetched note with $noteId. ${noteModel.get().title} ${noteModel.get().content}");
            noteTitle = noteModel.get().title;
            noteContent = noteModel.get().content;
            noteUpdatedOn = noteModel.get().updatedOn.format(formatter)
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteModel.save(title = noteTitle, content = noteContent)
            noteUpdatedOn = note.updatedOn.format(formatter)
        }
    }
}