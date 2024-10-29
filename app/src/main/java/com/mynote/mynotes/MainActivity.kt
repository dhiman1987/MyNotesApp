package com.mynote.mynotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.db.MyNotesDatabase
import com.mynote.mynotes.db.NoteRepositoryImpl
import com.mynote.mynotes.encryption.BiometricAuthHelper
import com.mynote.mynotes.encryption.EncryptionUtils
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModel
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModelFactory
import com.mynote.mynotes.ui.note.list.NoteListViewModel
import com.mynote.mynotes.ui.note.list.NoteListViewModelFactory
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
                    // NoteEditor(noteEditorViewModel)
                    NavHost(navController, startDestination = "home") {
                        composable("home") { MainScreen(navController) }
                        composable("list") { NoteList(noteListViewModel, navController) }
                        composable("detail/{noteId}") { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getString("noteId")
                            if (null != noteId) {
                                NoteEditor(noteId)
                            } else {
                                NoteEditor("")
                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
        Button(onClick = {
            navController.navigate("list")
        },
            modifier
                .width(IntrinsicSize.Max)
                .height(10.dp)
                .padding(5.dp)
        ) {
            Text("Go")
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun NoteEditor(
        noteId: String,
        modifier: Modifier = Modifier
    ) {
        var mode by remember { mutableStateOf("view") }
        val noteEditorViewModel: NoteEditorViewModel = viewModel(factory = NoteEditorViewModelFactory(noteRepository,noteId))
        val encryptionBiometricAuthHelper = remember {
            BiometricAuthHelper(this as FragmentActivity) { cipher ->
                val dataToEncrypt = noteEditorViewModel.noteContent
                Log.v(TAG, "dataToEncrypt $dataToEncrypt[${dataToEncrypt.length}]")
                val encryptedData = Base64.encodeToString(cipher.doFinal(dataToEncrypt.toByteArray()), Base64.DEFAULT)
                val iv =  Base64.encodeToString(cipher.iv, Base64.DEFAULT)
                Log.v(TAG, "iv $iv [${iv.length}] $encryptedData[${encryptedData.length}]")
                val combinedData  = "$iv|$encryptedData"
                Log.v(TAG, "combinedData $combinedData[${combinedData.length}]")
                noteEditorViewModel.noteContent = combinedData
                noteEditorViewModel.save()
            }
        }
        if("" != noteId && "view" == mode){
            Log.v(TAG, "fetched note with $noteId ${noteEditorViewModel.noteTitle}  ${noteEditorViewModel.noteContent}")
            val combinedStringContent =  noteEditorViewModel.noteContent
            if(combinedStringContent.indexOf("|")>-1){
                val combintedStringArr = combinedStringContent.split("|")
                //Log.v(TAG, "*** iv ${combintedStringArr[0]}[${combintedStringArr[0].length}] " +
                      //  "${combintedStringArr[1]}[${combintedStringArr[1].length}]")
                val ivEx = Base64.decode(combintedStringArr[0],Base64.DEFAULT)
                val encryptedDataEx = Base64.decode(combintedStringArr[1],Base64.DEFAULT)

                val decryptionBiometricAuthHelper = remember {
                    BiometricAuthHelper(this as FragmentActivity) { c ->
                        Log.v(
                            TAG, "#### iv ${ivEx.toString(Charsets.UTF_8)}[${ivEx.size}] " +
                                    "${encryptedDataEx.toString(Charsets.UTF_8)}[${encryptedDataEx.size}]"
                        )
                        val decryptedData = c.doFinal(encryptedDataEx)
                        noteEditorViewModel.noteContent = decryptedData.toString(Charsets.UTF_8)
                    }
                }

                val dcrypCipher = EncryptionUtils.getDecryptCipher(ivEx)
                decryptionBiometricAuthHelper.authenticate(dcrypCipher)
            }


        }

        Scaffold(
            topBar = {},
            bottomBar = {
                if (mode == "edit") {
                    Button(
                        onClick = {
                            Log.v(TAG, "Save button clicked")
                            val cipher = EncryptionUtils.getEncryptCipher()
                            encryptionBiometricAuthHelper.authenticate(cipher)
                            mode = "view"
                        },
                        modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Save",
                            modifier = modifier
                        )

                    }
                } else {
                    Button(
                        onClick = {
                            Log.v(TAG, "`Edit` button clicked");
                            mode = "edit"
                        },
                        modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Edit",
                            modifier = modifier
                        )

                    }
                }

            }
        ) {
            if (mode == "edit") {
                ShowEditor(noteEditorViewModel = noteEditorViewModel, modifier = modifier, it = it)
            } else {
                ShowViewer(noteEditorViewModel = noteEditorViewModel, modifier = modifier, it = it)
            }


        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun NoteList(
        noteListViewModel: NoteListViewModel,
        navController: NavController,
        modifier: Modifier = Modifier
    ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        title = {
                            Text("My Notes")
                        }
                    )
                },
                bottomBar = {
                    Button(
                        onClick = {
                            Log.v(TAG, "Create new note button clicked");
                            navController.navigate("detail/")
                        },
                        modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "New Note",
                            modifier = modifier
                        )

                    }
                }
            ) {
                Box(modifier = Modifier.padding(
                    start = 0.dp,
                    end = 0.dp,
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding()
                )) {
                    LazyColumn {
                        items(noteListViewModel.noteList) { note ->
                            NoteItem(note,
                                onClick = {
                                    Log.v(TAG, "Note ${note.id} clicked")
                                    navController.navigate("detail/${note.id}")
                                },
                                onDelete = {
                                    noteListViewModel.deleteNote(note.id)
                                })
                        }
                    }
                }
            }
    }


    @Composable
    fun NoteItem(note: NoteOverview, onClick: () -> Unit, onDelete: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically){
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = note.updatedOn,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Box(modifier = Modifier.padding(16.dp)){
                    Button( onClick = onDelete) { Text("Delete") }
                    }
                }
            }

    }

    @Composable
    private fun ShowViewer(
        noteEditorViewModel: NoteEditorViewModel,
        modifier: Modifier,
        it: PaddingValues
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ) {

            Text(
                text = noteEditorViewModel.noteTitle,
                fontSize = 30.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Text(
                text = noteEditorViewModel.noteUpdatedOn,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Text(
                text = noteEditorViewModel.noteContent, modifier = modifier
                    .padding(5.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            )
        }
    }

    @Composable
    private fun ShowEditor(
        noteEditorViewModel: NoteEditorViewModel,
        modifier: Modifier,
        it: PaddingValues
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ) {

            OutlinedTextField(
                value = noteEditorViewModel.noteTitle,
                onValueChange = { noteEditorViewModel.noteTitle = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = noteEditorViewModel.noteContent,
                onValueChange = { noteEditorViewModel.noteContent = it },
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}




