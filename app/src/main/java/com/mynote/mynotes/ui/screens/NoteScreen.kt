package com.mynote.mynotes.ui.screens

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.encryption.BiometricAuthHelper
import com.mynote.mynotes.encryption.EncryptionUtils
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModel
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModelFactory

val NoteScreenTag = "NoteViewerScreen"

@Composable
fun NoteScreen (noteId:String,
                navController: NavController,
                noteRepository: NoteRepository){
    val context = LocalContext.current
    val noteEditorViewModel: NoteEditorViewModel = viewModel(factory = NoteEditorViewModelFactory(noteRepository,noteId))
    val mode by noteEditorViewModel.mode.collectAsState()
    when (mode) {
        "view" -> {
            decryptContent(noteEditorViewModel, context)
            ShowViewer(
                noteEditorViewModel.noteTitle.collectAsState().value,
                noteEditorViewModel.noteContent.collectAsState().value,
                noteEditorViewModel.noteUpdatedOn,
                onEdit = {
                    Log.v(NoteScreenTag, "`Edit` button clicked")
                    noteEditorViewModel.setMode("edit")
                })
        }
        "edit" -> {
            ShowEditor(noteEditorViewModel, onSave = {
                Log.v(NoteScreenTag, "`Save` button clicked")
                encryptContent(noteEditorViewModel,context)
                noteEditorViewModel.setMode("view")
            })
        }
        else -> {
            throw IllegalStateException("Unknown state $mode")
        }
    }

}

private fun encryptContent(noteEditorViewModel: NoteEditorViewModel, context: Context){
    val dataToEncrypt = noteEditorViewModel.noteContent.value
    val encryptCipher = EncryptionUtils.getEncryptCipher()
    val biometricAuthHelper = BiometricAuthHelper(context as FragmentActivity) { cipher ->
        Log.v(NoteScreenTag, "dataToEncrypt $dataToEncrypt[${dataToEncrypt.length}]")
        val encryptedData = Base64.encodeToString(cipher.doFinal(dataToEncrypt.toByteArray()), Base64.DEFAULT)
        val iv =  Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        Log.v(NoteScreenTag, "iv $iv [${iv.length}] $encryptedData[${encryptedData.length}]")
        val combinedData  = "$iv|$encryptedData"
        Log.v(NoteScreenTag, "combinedData $combinedData[${combinedData.length}]")
        noteEditorViewModel.setNoteContent(combinedData)
        noteEditorViewModel.save()
    }
    biometricAuthHelper.authenticate(encryptCipher)
}

private fun decryptContent(noteEditorViewModel: NoteEditorViewModel, context: Context){
    val combinedStringContent = noteEditorViewModel.noteContent.value
    Log.v(NoteScreenTag, "fetched note with $combinedStringContent")
        if(combinedStringContent.indexOf("|")>-1){
            val combintedStringArr = combinedStringContent.split("|")
            val iv = Base64.decode(combintedStringArr[0], Base64.DEFAULT)
            val encryptedContent = Base64.decode(combintedStringArr[1], Base64.DEFAULT)
            val decrypCipher = EncryptionUtils.getDecryptCipher(iv)
            Log.v(NoteScreenTag, "iv $iv encryptedContent $encryptedContent")
            val decryptionBiometricAuthHelper =
                BiometricAuthHelper(context as FragmentActivity) { c ->
                    Log.v(NoteScreenTag, "---> iv $iv encryptedContent $encryptedContent")
                   noteEditorViewModel.setNoteContent(c.doFinal(encryptedContent).toString(Charsets.UTF_8))
                    Log.v(NoteScreenTag, "---> iv $iv decryptedContent [${noteEditorViewModel.noteContent.value}]")
                }
            decryptionBiometricAuthHelper.authenticate(decrypCipher)
        }

}