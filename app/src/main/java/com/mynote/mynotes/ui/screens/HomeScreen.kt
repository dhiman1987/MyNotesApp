package com.mynote.mynotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mynote.mynotes.ui.note.home.HomeViewModel
import com.mynote.mynotes.ui.theme.MyNotesTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import com.mynote.mynotes.R


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    val searchText by homeViewModel.searchText.collectAsState()
    Column( modifier = Modifier .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center ) {

        SvgImage()
        HomeTagLineText()
        Spacer(Modifier.height(50.dp))
        HomeMainHeroText()
        Spacer(Modifier.height(50.dp))
        HomeCreateNewNoteButton(navController)
        HomeCreateListNotesButton(navController)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = false)
@Composable
fun HomePreview(){
    MyNotesTheme(darkTheme = true) {
        HomeScreen(
            navController = NavController(context = LocalContext.current),
            modifier = Modifier
        )
    }
}

@Composable
fun HomeTagLineText(){
    Text(text = "private.secure.offline",
            style = TextStyle(
            fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSecondary,
        textAlign = TextAlign.Center),
    modifier = Modifier .fillMaxWidth()
        .padding(16.dp,0.dp,16.dp,5.dp))
}

@Composable
fun HomeMainHeroText(){
    Text( text = "Write your thoughts without a worry",
        style = TextStyle(
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Left),
        modifier = Modifier .fillMaxWidth()
            .padding(16.dp,16.dp,16.dp,5.dp))

    Text( text = "Your data stays with you",
        style = TextStyle(
            fontSize = 46.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Left),
        modifier = Modifier .fillMaxWidth()
            .padding(16.dp,5.dp,16.dp,16.dp) )
}

@Composable
fun HomeCreateNewNoteButton(navController: NavController){
    Button(
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary ),
        onClick = {navController.navigate("note/")})
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                Icons.Filled.Edit, contentDescription = "Create a new note",
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "Create a new note",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@Composable
fun HomeCreateListNotesButton(navController: NavController){
    Button(
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary ),
        onClick = {navController.navigate("list/")})
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                Icons.Filled.List, contentDescription = "list notes",
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = "View your notes",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@Composable fun SvgImage(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val imageResource = if (isDarkTheme) {
        R.drawable.dot_and_dash_dark
    } else {
        R.drawable.dot_and_dash_light }
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = "Dot and Dash",
        modifier = modifier
            .width(140.dp)
            .height(140.dp)
    )
}
