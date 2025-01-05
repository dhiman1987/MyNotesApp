package com.mynote.mynotes.ui.screens.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mynote.mynotes.TAG
import com.mynote.mynotes.data.NoteOverview

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteList(
    noteList: List<NoteOverview>,
    onDelete: (String) -> Unit,
    onItemClick: (String) -> Unit,
    it: PaddingValues
) {

    Box(modifier = Modifier.padding(
        start = 0.dp,
        end = 0.dp,
        bottom = it.calculateBottomPadding(),
        top = it.calculateTopPadding(),
    )) {
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(noteList) { note ->
                NoteItem(note,
                    onClick = {
                        Log.v(TAG, "Note ${note.id} clicked")
                        onItemClick(note.id)},
                    /*onClick = {

                        navController.navigate("note/${note.id}")
                    },*/
                    onDelete = {
                        Log.v(TAG, "Deleting ${note.id} clicked")
                        onDelete(note.id)
                    })
            }
        }
    }
}

@Composable
fun NoteItem(note: NoteOverview, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row( modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(16.dp,4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = note.updatedOn,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Box(modifier = Modifier
                .padding(16.dp)){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        containerColor = MaterialTheme.colorScheme.secondary ),
                    onClick = onDelete)
                {
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Icon(
                            Icons.Filled.Delete, contentDescription = "Delete",
                            modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }

                }
            }
        }
    }

}