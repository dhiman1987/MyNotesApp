package com.dotndash.mynotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dotndash.mynotes.data.Tag
import com.dotndash.mynotes.db.DummyNoteRepository
import com.dotndash.mynotes.ui.note.editor.NoteEditorViewModel
import com.dotndash.mynotes.ui.screens.common.ShowTags
import com.dotndash.mynotes.ui.theme.DotnDashTheme

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
            ShowTagsSection(noteEditorViewModel)
            Spacer(modifier = Modifier.width(8.dp))
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

@Composable
fun ShowTagsSection(noteEditorViewModel: NoteEditorViewModel){
    var tagInput by remember { mutableStateOf("") }
    val onDelete: (Tag) -> Unit = { tag -> noteEditorViewModel.removeTag(tag) }
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("New Tag - not encrypted") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.height(58.dp),
                shape = RoundedCornerShape(20),
                onClick = {
                noteEditorViewModel.addTag(tagInput)
                tagInput = ""
            }) {
                Text("Add Tag")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        ShowTags(tags=noteEditorViewModel.tags,
            deleteEnabled = true,
            backgroundColor = Color.Transparent,
            onDelete = onDelete)
    }
}

@Composable
@Preview
fun ShowEditorPreview(){

    val noteEditorViewModel = NoteEditorViewModel(
        noteId = "",
        noteRepository = DummyNoteRepository()
    )
    DotnDashTheme(darkTheme = true) {
        ShowEditor(noteEditorViewModel = noteEditorViewModel,
            onSave = {}
            )
    }
}
