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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class NoteEditorViewModel(noteId: String, noteRepository: NoteRepository) : ViewModel() {
    private val TAG = "NoteEditorViewModel"
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    private var noteModel: NoteModel = NoteModel("",noteRepository)
    private val _mode = MutableStateFlow("view")
    val mode: StateFlow<String> = _mode

    private val _noteContent = MutableStateFlow(noteModel.get().content)
    val noteContent: StateFlow<String> = _noteContent

    private val _noteTitle = MutableStateFlow(noteModel.get().content)
    var noteTitle: StateFlow<String> = _noteTitle

    var noteUpdatedOn by mutableStateOf<String>(noteModel.get().updatedOn.format(formatter))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v(TAG, "fetching note $noteId")
            if(noteId.isBlank()){
                setMode("edit")
            }
            noteModel = NoteModel(noteId,noteRepository)
            Log.v(TAG, "fetched note with $noteId. ${noteModel.get().title} ${noteModel.get().content}")
            setNoteTitle(noteModel.get().title)
            setNoteContent(noteModel.get().content)
            noteUpdatedOn = noteModel.get().updatedOn.format(formatter)
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteModel.save(title = noteTitle.value, content = noteContent.value)
            noteUpdatedOn = note.updatedOn.format(formatter)
        }
    }

    fun setMode(newMode: String) { viewModelScope.launch { _mode.value = newMode } }
    fun setNoteContent(newNoteContent: String) { viewModelScope.launch { _noteContent.value = newNoteContent } }
    fun setNoteTitle(newNoteTitle: String) { viewModelScope.launch { _noteTitle.value = newNoteTitle } }
}