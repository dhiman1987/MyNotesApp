package com.mynote.mynotes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mynote.mynotes.db.MyNotesDatabase
import com.mynote.mynotes.db.NoteRepositoryImpl
import com.mynote.mynotes.encryption.EncryptionUtils
import com.mynote.mynotes.ui.note.list.NoteListViewModel
import com.mynote.mynotes.ui.note.list.NoteListViewModelFactory
import com.mynote.mynotes.ui.screens.HomeScreen
import com.mynote.mynotes.ui.screens.NoteListScreen
import com.mynote.mynotes.ui.screens.NoteScreen
import com.mynote.mynotes.ui.theme.MyNotesTheme

val TAG = "MainActivity"
class MainActivity : FragmentActivity() {

    private val database by lazy { MyNotesDatabase.getDatabase(applicationContext) }
    private val noteRepository by lazy { NoteRepositoryImpl(database.notesDao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            EncryptionUtils.initialize()
            MyNotesTheme {
                val noteListViewModel: NoteListViewModel by viewModels {
                    NoteListViewModelFactory(
                        repository = noteRepository
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController) }
                        composable("list") { NoteListScreen(noteListViewModel, navController) }
                        composable("note/{noteId}") { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getString("noteId")
                            if (null != noteId) {
                                NoteScreen(noteId,navController,noteRepository)
                            } else {
                                NoteScreen("",navController,noteRepository)
                            }

                        }
                    }
                }
            }
        }
    }
}




