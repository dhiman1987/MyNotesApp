package com.dotndash.mynotes.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.dotndash.mynotes.R
import com.dotndash.mynotes.ui.theme.DotnDashTheme


@Composable
fun HomeScreen(navController: NavController) {
    Column( modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom ) {

        SvgImage()
        HomeTagLineText()
        Spacer(Modifier.height(50.dp))
        HomeMainHeroText()
        Spacer(Modifier.height(50.dp))
        HomeListNotesButton(navController)
        HomeImportNotes(navController)
        HomeCreateNewNoteButton(navController)

    }

}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    DotnDashTheme(darkTheme = false) {
        HomeScreen(
            navController = NavController(context = LocalContext.current)
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
fun HomeImportNotes(navController: NavController){
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary ),
        onClick = {navController.navigate("import")})
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(
                Icons.Filled.Add, contentDescription = "Import notes",
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "Import notes",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}
@Composable
fun HomeCreateNewNoteButton(navController: NavController){
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(20),
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
fun HomeListNotesButton(navController: NavController){
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(20),
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
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
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

