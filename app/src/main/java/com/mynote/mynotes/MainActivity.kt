package com.mynote.mynotes

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.mynote.mynotes.file.FileRepository
import com.mynote.mynotes.ui.note.home.HomeViewModel
import com.mynote.mynotes.ui.note.list.NoteListViewModel
import com.mynote.mynotes.ui.note.list.NoteListViewModelFactory
import com.mynote.mynotes.ui.screens.HomeScreen
import com.mynote.mynotes.ui.screens.ImportNoteScreen
import com.mynote.mynotes.ui.screens.NoteScreen
import com.mynote.mynotes.ui.screens.ViewNotesScreen
import com.mynote.mynotes.ui.theme.MyNotesTheme

val TAG = "MainActivity"
class MainActivity : FragmentActivity() {

    private val database by lazy { MyNotesDatabase.getDatabase(applicationContext) }
    private val noteRepository by lazy { NoteRepositoryImpl(database.notesDao()) }
    private val fileRepository by lazy {FileRepository(applicationContext)}

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
                        composable("home") { HomeScreen(HomeViewModel(), navController) }
                        composable("import") { ImportNoteScreen(noteRepository,fileRepository, navController) }
                        composable("list/{searchText}"){backStackEntry ->
                            val searchText = backStackEntry.arguments?.getString("searchText")
                            noteListViewModel.searchNotes(searchText,null,null)
                            ViewNotesScreen(noteListViewModel, navController) }
                        composable("note/{noteId}") { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getString("noteId")
                            if (null != noteId) {
                                NoteScreen(noteId,noteRepository)
                            } else {
                                NoteScreen("",noteRepository)
                            }

                        }
                    }
                }
            }
        }
    }
@SuppressLint("MissingSuperCall")
override fun onRequestPermissionsResult(requestCode: Int,
                                        permissions:
                                         Array<String>, grantResults: IntArray ) {
    if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Log.v("Main", "Permission granted")
        } else {
            Log.v("Main", "Permission rejected $requestCode")
        }

        }
    }

    companion object { const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 0x01FF }
}




