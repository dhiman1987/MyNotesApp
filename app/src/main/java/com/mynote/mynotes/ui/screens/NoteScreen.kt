package com.mynote.mynotes.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.encryption.BiometricAuthHelper
import com.mynote.mynotes.encryption.EncryptionUtils
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModel
import com.mynote.mynotes.ui.note.editor.NoteEditorViewModelFactory

const val NOTE_SCREEN_TAG = "NoteViewerScreen"

@Composable
fun NoteScreen (noteId:String,
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
                    Log.v(NOTE_SCREEN_TAG, "`Edit` button clicked")
                    noteEditorViewModel.setMode("edit")
                },
                onShare = {
                    shareNote(context,"""
 ${noteEditorViewModel.noteTitle.value}
 
 ${noteEditorViewModel.noteContent.value}
                    """.trimIndent())
                })
        }
        "edit" -> {
            ShowEditor(noteEditorViewModel, onSave = {
                Log.v(NOTE_SCREEN_TAG, "`Save` button clicked")
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
        Log.v(NOTE_SCREEN_TAG, "dataToEncrypt $dataToEncrypt[${dataToEncrypt.length}]")
        val encryptedData = Base64.encodeToString(cipher.doFinal(dataToEncrypt.toByteArray()), Base64.DEFAULT)
        val iv =  Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        val combinedData  = "$iv|$encryptedData"
        Log.v(NOTE_SCREEN_TAG, "encrypted data $combinedData[${combinedData.length}]")
        noteEditorViewModel.setNoteContent(combinedData)
        noteEditorViewModel.setStrongEncryption(true)
        noteEditorViewModel.save()
    }
    biometricAuthHelper.authenticate(encryptCipher)
}

private fun decryptContent(noteEditorViewModel: NoteEditorViewModel, context: Context){
    val combinedStringContent = noteEditorViewModel.noteContent.value
    Log.v(NOTE_SCREEN_TAG, "fetched note with $combinedStringContent")
        if(combinedStringContent.indexOf("|")>-1){
            val combinedStringArr = combinedStringContent.split("|")
            val iv = Base64.decode(combinedStringArr[0], Base64.DEFAULT)
            val encryptedContent = Base64.decode(combinedStringArr[1], Base64.DEFAULT)
            Log.v(NOTE_SCREEN_TAG, "iv $iv encryptedContent $encryptedContent")
            val decryptionBiometricAuthHelper =
                BiometricAuthHelper(context as FragmentActivity) { c ->
                    Log.v(NOTE_SCREEN_TAG, "---> iv $iv encryptedContent $encryptedContent")
                    noteEditorViewModel.setNoteContent(c.doFinal(encryptedContent).toString(Charsets.UTF_8))
                    Log.v(NOTE_SCREEN_TAG, "---> iv $iv decryptedContent [${noteEditorViewModel.noteContent.value}]")
                }
            if(noteEditorViewModel.strongEncryption.value){
                Log.v(NOTE_SCREEN_TAG,"Strong encryption detected")
                decryptionBiometricAuthHelper.authenticate(EncryptionUtils.getDecryptCipher(iv))
            } else {
                Log.v(NOTE_SCREEN_TAG,"Weak encryption detected")
                val importCipher = EncryptionUtils.getImportDecryptCipher(iv)
                Log.v(NOTE_SCREEN_TAG, "---> iv $iv weak encrypted content $encryptedContent")
                noteEditorViewModel.setNoteContent(importCipher.doFinal(encryptedContent).toString(Charsets.UTF_8))
                Log.v(NOTE_SCREEN_TAG, "---> iv $iv weak decrypted content [${noteEditorViewModel.noteContent.value}]")
            }

        }

}

private fun shareNote(context: Context, noteText: String){
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, noteText)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}