package com.mynote.mynotes.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mynote.mynotes.TAG
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.ui.note.list.NoteListViewModel
import com.mynote.mynotes.ui.screens.common.NoteList
import com.mynote.mynotes.ui.theme.MyNotesTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewNotesScreen(
    noteListViewModel: NoteListViewModel,
    navController: NavController
){
    val onDelete: (String) -> Unit = { id -> noteListViewModel.deleteNote(id) }
    val onSearch: (String, LocalDate, LocalDate) -> Unit = {
        s: String, fd: LocalDate, td: LocalDate -> noteListViewModel.searchNotes(s,fd,td)}
    val onItemClick: (String) -> Unit = { id -> navController.navigate("note/$id")}
    ViewNotes(noteListViewModel.noteList,onDelete,onSearch,navController,onItemClick)
}

@Composable
fun ViewNotes( noteList: List<NoteOverview>,
               onDelete: (String) -> Unit,
               onSearch: (String, LocalDate, LocalDate) -> Unit,
               navController: NavController,
               onItemClick: (String) -> Unit){
    Scaffold(
        topBar = { ViewNotesTopBar(onSearch = onSearch) },
        bottomBar = { ViewNotesBottomBar(navController) }
    ) {
        NoteList(noteList, onDelete = onDelete, onItemClick = onItemClick, it = it)
    }
}

@Composable
fun ViewNotesTopBar(onSearch: (String, LocalDate, LocalDate) -> Unit){
    var searchText by remember { mutableStateOf("") }
    var dateFrom by remember { mutableStateOf(LocalDate.now().minusMonths(1L)) }
    var dateTo by remember { mutableStateOf(LocalDate.now()) }

    Column( modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .padding(16.dp) ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search by note title") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround ) {
            DatePickerField(label = "Date From",
                selectedDate = dateFrom,
                onDateChange = { dateFrom = it })
            Spacer(modifier = Modifier.width(8.dp))
            DatePickerField(label = "Date To",
                selectedDate = dateTo,
                onDateChange = { dateTo = it })

        Spacer(modifier = Modifier.height(32.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary ),
                onClick = {
                    Log.v("ViewNotes","dateFrom $dateFrom dateTo $dateTo")
                            onSearch(searchText,dateFrom,dateTo)
                          },
                modifier = Modifier
                    .align(Alignment.Bottom))
            { Icon(Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun DatePickerField(label: String,
                                selectedDate: LocalDate,
                                onDateChange: (LocalDate) -> Unit) {
    var inputText by remember { mutableStateOf(selectedDate.format(DateTimeFormatter.ISO_DATE)) }
OutlinedTextField( value = inputText,
    onValueChange = {
        if (it.length<11){
            inputText = it
            if(it.length == 10){
                Log.v("ViewNotes","date selected $inputText")
                try{
                    val newDate = LocalDate.parse(inputText, DateTimeFormatter.ISO_DATE)
                    onDateChange(newDate)
                }catch (ex: Exception){
                    Log.e("ViewNotes","invalid date selected $inputText")
                }

            }
        }
                    },
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    colors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
        cursorColor = MaterialTheme.colorScheme.onPrimary,
        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary ),
    label = { Text(text=label,
        color = MaterialTheme.colorScheme.onPrimary
    ) },
    modifier = Modifier.width(132.dp))
}




@Composable
fun ViewNotesBottomBar(navController: NavController){
    Button(
        onClick = {
            Log.v(TAG, "Create new note button clicked")
            navController.navigate("note/")
        },
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(46.dp),
    ) {
        Text(
            text = "New Note",
            modifier = Modifier,
            style = MaterialTheme.typography.titleMedium
        )

    }
}



@Preview(showBackground = true)
@Composable
fun NoteItemPreview(modifier: Modifier = Modifier){

    val noteList = listOf(
        NoteOverview("11", "This is a note 1", true,"02 Nov 2024"),
        NoteOverview("12", "This is a note 2", true,"03 Nov 2024"),
        NoteOverview("13", "This is a note 3", false,"04 Nov 2024"),
        NoteOverview("14", "This is a note 4", true,"05 Nov 2024"),
     //   NoteOverview("15", "This is a note 5", "06 Nov 2024"),
     //   NoteOverview("16", "This is a note 6", "07 Nov 2024")
    )

    MyNotesTheme(darkTheme = false) {

        val onSearch: (String, LocalDate, LocalDate) -> Unit = {
                s: String, fd: LocalDate, td: LocalDate -> Log.v("preview","$s $fd $td")}
        ViewNotes(
            noteList = noteList,
            onDelete = {},
            onSearch = onSearch,
            onItemClick = {},
            navController = NavController(context = LocalContext.current)
        )
    }
}