package com.mynote.mynotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModel

@Composable
fun ShowEditor(
    noteEditorViewModel: NoteEditorViewModel,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {},
        bottomBar = {
            Button(
                onClick = onSave,
                modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(20),
            ) {
                Text(text = "Save",
                    modifier = modifier,
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = noteEditorViewModel.noteTitle.collectAsState().value,
                onValueChange = { noteEditorViewModel.setNoteTitle(it) },
                label = { Text("Note Title - not encrypted") },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = noteEditorViewModel.noteContent.collectAsState().value,
                onValueChange = { noteEditorViewModel.setNoteContent(it) },
                label = { Text("Note Content - encrypted ") },
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}
