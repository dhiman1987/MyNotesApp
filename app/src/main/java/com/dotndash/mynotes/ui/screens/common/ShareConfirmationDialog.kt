package com.dotndash.mynotes.ui.screens.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dotndash.mynotes.ui.theme.DotnDashTheme

@Composable
fun ConfirmationDialog(
    titleText:  String,
    contentText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titleText) },
        text = { Text(text = contentText.trimIndent()) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
@Preview
fun ShareConfirmationDialogPreview(){
    DotnDashTheme(darkTheme = false) {
        ConfirmationDialog(
            titleText = "Share Confirmation",
            contentText = """
            Are you sure you want to share this content?
            
            You will be asked to perform Biometric authentication. If this succeeds, your data will be shared as plain text with the sharing application. It will no longer be encrypted. 
            
            Please choose the application wisely.
        """,
            onConfirm = { },
            onDismiss = {}
        )
    }
}


