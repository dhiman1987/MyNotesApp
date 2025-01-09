package com.mynote.mynotes.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.ui.note.import.NoteImportViewModel
import com.mynote.mynotes.ui.note.import.NoteImportViewModelFactory
import com.mynote.mynotes.ui.screens.common.NoteList
import com.mynote.mynotes.ui.theme.MyNotesTheme
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val TAG = "ImportNotes"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImportNoteScreen(noteRepository: NoteRepository){
    val noteImportViewModel: NoteImportViewModel = viewModel(factory = NoteImportViewModelFactory(noteRepository))
    val deleteSelectedNote: (String) -> Unit = { id -> noteImportViewModel.deleteSelectedNote(id)}
    val updateSelectedNotes: (List<NoteOverview>) -> Unit = { updatedList -> noteImportViewModel.updateSelectedNotes(updatedList)}
    ImportNote(context = LocalContext.current,
        noteImportViewModel.selectedNotes,
        deleteSelectedNote = deleteSelectedNote,
        updateSelectedNotes = updateSelectedNotes)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImportNote(context: Context,
               selectedNotes:List<NoteOverview>,
               deleteSelectedNote: (String) -> Unit,
               updateSelectedNotes: (List<NoteOverview>) -> Unit
               ){

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")



    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ){ uris: List<Uri> ->
        val selectedNotesInt = ArrayList<NoteOverview>()
        uris.forEach { uri ->
            if(uri.path!=null){
                context.contentResolver.openInputStream(uri)?.let { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    val noteId = uri.path
                    var noteTitle = reader.readText()
                    if(noteTitle.length>20){
                        noteTitle = noteTitle.substring(0,20)
                        noteTitle = noteTitle.substring(0,noteTitle.lastIndexOf(" "))
                    }
                    val noteOverview = NoteOverview(id=noteId!!, title=noteTitle,
                        updatedOn = LocalDateTime.now().format(formatter))
                    selectedNotesInt.add(noteOverview)
                    Log.v(TAG, noteOverview.title)
                    reader.close()
                }
                Log.v(TAG, "selectedNotesInt size ${selectedNotesInt.size}")
                updateSelectedNotes(selectedNotesInt)
                Log.v(TAG, "selectedNotes size ${selectedNotes.size}")
            }

        }
    }

    val importNotes: () -> Unit = {
        Log.v(TAG, "importing notes ${selectedNotes.size}")
        selectedNotes.map { note -> {
            Log.v(TAG, "${note.id} [${note.title}]")
        } }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission() )
    { isGranted: Boolean ->
        if (isGranted) {
            Log.v(TAG, "Granted permission")
            pickFileLauncher.launch("text/plain")
        } else {
            Log.v(TAG, "Rejected permission")
        }
    }

    val onBrowseButtonClick:() -> Unit ={
        Log.v(TAG, "import button clicked")
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE ) == PERMISSION_GRANTED ) {
            Log.v(TAG, "Have permission")
            pickFileLauncher.launch("text/plain")
        } else {
            Log.v(TAG, "Ask permission")
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            Log.v(TAG, "Asked permission")
            pickFileLauncher.launch("text/plain")
        }
    }

    Scaffold (
        topBar = { AppBar(onBrowseButtonClick= onBrowseButtonClick) },
        bottomBar = { BottomBar(importNotes = importNotes)})
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NoteList(selectedNotes,
                    onDelete = deleteSelectedNote,
                    onItemClick = {},
                    it = it)
            }
        }


}

@Composable
fun AppBar(onBrowseButtonClick:()-> Unit){
    Column( modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .padding(16.dp) ) {
        BrowseNotesButton(onClick = onBrowseButtonClick)
    }

}
@Composable
fun BottomBar(importNotes: () -> Unit){
    ImportNotesButton(onClick = importNotes)
}

@Composable
fun ImportNotesButton(onClick: () -> Unit){
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary ),
        onClick = onClick)
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                Icons.Filled.List, contentDescription = "Import note",
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "Import",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@Composable
fun BrowseNotesButton(onClick:()-> Unit){
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary ),
        onClick = onClick)
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                Icons.Filled.Search, contentDescription = "Browse note",
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "Browse note",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun ImportNotePreview(){
    MyNotesTheme(darkTheme = false) {
        ImportNote(
            context = LocalContext.current,
            selectedNotes = emptyList(),
            deleteSelectedNote = {},
            updateSelectedNotes = {}
        )
    }
}