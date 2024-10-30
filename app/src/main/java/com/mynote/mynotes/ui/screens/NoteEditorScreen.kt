package com.mynote.mynotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
                modifier.fillMaxWidth().padding(5.dp)
            ) {
                Text(text = "Save", modifier = modifier)
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            OutlinedTextField(
                value = noteEditorViewModel.noteContent.collectAsState().value,
                onValueChange = { noteEditorViewModel.setNoteContent(it) },
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}
