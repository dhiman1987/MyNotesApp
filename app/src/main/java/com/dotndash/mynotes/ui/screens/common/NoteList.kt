package com.dotndash.mynotes.ui.screens.common

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dotndash.mynotes.TAG
import com.dotndash.mynotes.data.NoteOverview
import com.dotndash.mynotes.data.Tag
import com.dotndash.mynotes.ui.theme.DotnDashTheme
import java.util.ArrayList

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
        var backgroundColor = MaterialTheme.colorScheme.surface
        if(!note.strongEncryption){
            backgroundColor = MaterialTheme.colorScheme.tertiary
        }
        Row( modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = note.updatedOn,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if(!note.strongEncryption){
                    Text(
                        text="Save once to strongly encrypt",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.padding(0.dp,10.dp)
                    )
                }
            }
            Box(modifier = Modifier
                .padding(16.dp)){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        containerColor = MaterialTheme.colorScheme.secondary),
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
        ShowTags(
            note.tags, backgroundColor = backgroundColor,
            deleteEnabled = false,
            onDelete = {}
        )
    }

}




@Preview(showBackground = false)
@Composable
fun NoteListPreview(){

    val tags = ArrayList<Tag>(3)
    tags.add(Tag(1L,"Journal"))
    tags.add(Tag(2L,"Office"))
    tags.add(Tag(3L,"Confidential"))

    val noteList = listOf(
        NoteOverview("11", "This is a note 1", true,tags,"02 Nov 2024"),
        NoteOverview("12", "This is a note 2", true,null,"03 Nov 2024"),
        NoteOverview("13", "This is a note 3", false,null,"04 Nov 2024"),
        NoteOverview("14", "This is a note 4", true,tags,"05 Nov 2024"),
        NoteOverview("15", "This is a note 5", true,null,"06 Nov 2024"),
        NoteOverview("16", "This is a note 6", false,tags,"07 Nov 2024")
    )
    val it = PaddingValues()
    DotnDashTheme(darkTheme = false) {
        NoteList(
            noteList = noteList,
            onDelete = { },
            onItemClick = { },
            it = it
        )
    }

}