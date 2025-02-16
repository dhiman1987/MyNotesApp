package com.mynote.mynotes.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mynote.mynotes.ui.screens.common.ConfirmationDialog
import com.mynote.mynotes.ui.screens.common.MarkdownText
import com.mynote.mynotes.ui.theme.MyNotesTheme

val NoteViewerScreenTag = "NoteViewerScreen"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowViewer(
    noteTitle: String,
    noteContent: String,
    noteUpdatedOn: String,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
 Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(text = noteTitle) },
                actions = {
                    IconButton(onClick = {
                            showDialog = true
                    },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                        Icon(
                            Icons.Filled.Share, contentDescription = "share note",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onEdit,
                modifier.fillMaxWidth().padding(5.dp)
            ) {
                Text(text = "Edit", modifier = modifier)
            }
        }
    ) {
     if (showDialog) {
         ConfirmationDialog(
             titleText = "Sharing Confirmation",
             contentText = """
                                Are you sure you want to share?
                                Once you share the note will be decrypted and shared in plain text with the shared application.
                                                            
                                Please choose the application carefully""".trimIndent(),
             onConfirm = {
                 showDialog = false
                 onShare()
             },
             onDismiss = { showDialog = false }
         )
     }

        Column(
            modifier = Modifier
                .padding(
                    start = 0.dp,
                    end = 0.dp,
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            Text(
                text = noteUpdatedOn,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            MarkdownText(
                text = noteContent, modifier = modifier
                    .padding(5.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            )
        }
    }

}

@Composable
@Preview
fun NoteScreenViewerPreview(){
    MyNotesTheme(darkTheme = true) {
        ShowViewer(
            noteTitle = "This is a note",
            noteContent = """
                # This is the heading,
                ## This is the sub heading text
                This is some normal text content. This is some normal text content. This is some normal text content. This is some normal text content.
                This is some normal text content. This is some normal text content. This is some normal text content. This is some normal text content.
                This is some normal text content. This is some normal text content. This is some normal text content. This is some normal text content.
                
                - Point one
                  - Sub point 1
                  - Sub point 2
                - Point 2
                - Pont 3 
                
                Ok so this **Is a very important note**
                
                Note: *Keep this in mind*
               
                Do you __like__ this styling?
                
                
            """.trimIndent(),
            noteUpdatedOn = "12th Nov 2025",
            onEdit = {},
            onShare = {}
        )
    }
}
