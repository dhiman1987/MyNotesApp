package com.dotndash.mynotes.ui.note.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotndash.mynotes.db.NoteRepository

class NoteEditorViewModelFactory(private val repository: NoteRepository, private var id:String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteEditorViewModel(id,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}