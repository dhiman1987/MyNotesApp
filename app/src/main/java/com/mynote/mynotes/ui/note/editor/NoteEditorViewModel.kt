package com.mynote.mynotes.ui.note.editor

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mynote.mynotes.data.Tag
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
    private val _strongEncryption = MutableStateFlow(noteModel.get().strongEncryption)
    val strongEncryption: StateFlow<Boolean> = _strongEncryption

    private val _noteContent = MutableStateFlow(noteModel.get().content)
    val noteContent: StateFlow<String> = _noteContent

    private val _noteTitle = MutableStateFlow(noteModel.get().content)
    var noteTitle: StateFlow<String> = _noteTitle

    var noteUpdatedOn by mutableStateOf<String>(noteModel.get().updatedOn.format(formatter))

    private val _tags = mutableStateListOf<Tag>()
    val tags: List<Tag> get() = _tags

    private var tagUpdate = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v(TAG, "fetching note $noteId")
            if(noteId.isBlank()){
                setMode("edit")
            }
            noteModel = NoteModel(noteId,noteRepository)
            Log.v(TAG, "fetched note with " +
                    "id=$noteId. " +
                    "title=${noteModel.get().title} " +
                    "content=${noteModel.get().content}" +
                    "tags size=${noteModel.get().tags?.size}")
            setNoteTitle(noteModel.get().title)
            setNoteContent(noteModel.get().content)
            setStrongEncryption(noteModel.get().strongEncryption)
            noteUpdatedOn = noteModel.get().updatedOn.format(formatter)
            if(noteModel.get().tags!=null){
                Log.v(TAG, "setting tags")
                setTags(noteModel.get().tags!!)
            }
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            if(!tagUpdate){
                val note = noteModel.save(
                    title = noteTitle.value, content = noteContent.value, strongEncryption = true,
                    tags = null
                )
                noteUpdatedOn = note.updatedOn.format(formatter)
            } else {
                val note = noteModel.save(
                    title = noteTitle.value, content = noteContent.value, strongEncryption = true,
                    tags = tags
                )
                tagUpdate=false
                noteUpdatedOn = note.updatedOn.format(formatter)
            }
        }
    }

    fun addTag(tagName: String) {
        if (tagName.isNotBlank()) {
            val newTag = Tag(id = 0L, name = tagName)
            _tags.add(newTag)
            tagUpdate=true
        }
    }

    fun removeTag(tag: Tag) {
        _tags.remove(tag)
        tagUpdate=true
    }

    fun setTags(tags:List<Tag>) {viewModelScope.launch { _tags.addAll(tags) }}
    fun setMode(newMode: String) { viewModelScope.launch { _mode.value = newMode } }
    fun setNoteContent(newNoteContent: String) { viewModelScope.launch { _noteContent.value = newNoteContent } }
    fun setNoteTitle(newNoteTitle: String) { viewModelScope.launch { _noteTitle.value = newNoteTitle } }
    fun setStrongEncryption(newEncryption: Boolean) { viewModelScope.launch { _strongEncryption.value = newEncryption } }
}