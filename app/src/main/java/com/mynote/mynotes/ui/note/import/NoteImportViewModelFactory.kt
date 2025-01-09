package com.mynote.mynotes.ui.note.import

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mynote.mynotes.db.NoteRepository

class NoteImportViewModelFactory (private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteImportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteImportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}