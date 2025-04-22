package com.dotndash.mynotes

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
import com.dotndash.mynotes.db.MyNotesDatabase
import com.dotndash.mynotes.db.NoteRepositoryImpl
import com.dotndash.mynotes.encryption.EncryptionUtils
import com.dotndash.mynotes.file.FileRepository
import com.dotndash.mynotes.ui.note.home.HomeViewModel
import com.dotndash.mynotes.ui.note.list.NoteListViewModel
import com.dotndash.mynotes.ui.note.list.NoteListViewModelFactory
import com.dotndash.mynotes.ui.screens.HomeScreen
import com.dotndash.mynotes.ui.screens.ImportNoteScreen
import com.dotndash.mynotes.ui.screens.NoteScreen
import com.dotndash.mynotes.ui.screens.ViewNotesScreen
import com.dotndash.mynotes.ui.theme.DotnDashTheme

val TAG = "MainActivity"
class MainActivity : FragmentActivity() {

    private val database by lazy { MyNotesDatabase.getDatabase(applicationContext) }
    private val noteRepository by lazy { NoteRepositoryImpl(database.notesDao()) }
    private val fileRepository by lazy { FileRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            EncryptionUtils.initialize()
            DotnDashTheme {
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




