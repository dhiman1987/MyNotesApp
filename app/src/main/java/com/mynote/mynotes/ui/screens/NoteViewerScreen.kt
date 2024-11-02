package com.mynote.mynotes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val NoteViewerScreenTag = "NoteViewerScreen"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowViewer(
    noteTitle: String,
    noteContent: String,
    noteUpdatedOn: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {

 Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(text = noteTitle) }
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
            Text(
                text = noteContent, modifier = modifier
                    .padding(5.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}
