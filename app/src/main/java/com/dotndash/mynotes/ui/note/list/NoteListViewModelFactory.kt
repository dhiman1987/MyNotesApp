package com.dotndash.mynotes.ui.note.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotndash.mynotes.db.NoteRepository

class NoteListViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}