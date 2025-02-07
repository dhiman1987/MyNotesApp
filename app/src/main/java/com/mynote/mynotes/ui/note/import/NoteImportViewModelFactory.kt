package com.mynote.mynotes.ui.note.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.file.FileRepository

class NoteImportViewModelFactory (private val repository: NoteRepository,
                                  private val fileRepository: FileRepository,
                                    private val navController: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteImportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteImportViewModel(repository, fileRepository, navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}