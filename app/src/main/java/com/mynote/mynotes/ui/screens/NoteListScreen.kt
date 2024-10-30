package com.mynote.mynotes.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mynote.mynotes.TAG
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.ui.note.list.NoteListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteListScreen(
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
                    Log.v(TAG, "Create new note button clicked")
                    navController.navigate("note/")
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
                            navController.navigate("note/${note.id}")
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