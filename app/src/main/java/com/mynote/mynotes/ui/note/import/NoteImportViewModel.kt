package com.mynote.mynotes.ui.note.import

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mynote.mynotes.data.NoteOverview
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.encryption.EncryptionUtils
import com.mynote.mynotes.file.FileRepository
import com.mynote.mynotes.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteImportViewModel (private val noteRepository: NoteRepository,
                           private val fileRepository: FileRepository,
                           private val navController: NavController
) : ViewModel() {
    private val TAG = "NoteImportViewModel"
   var selectedNotes = mutableStateListOf<NoteOverview>()
        private set
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")

    fun deleteSelectedNote(id:String){
        selectedNotes.removeAll { note -> note.id == id }
    }

    fun updateSelectedNotes(filesToImport:List<Uri>) {
        selectedNotes.clear()
        viewModelScope.launch(Dispatchers.IO) {
            filesToImport.forEach { uri ->
                if(uri.path!=null){
                    val fileContent = fileRepository.readFile(uri)
                    val noteId = uri.toString()
                    var noteTitle = fileContent
                    if(noteTitle.length>20){
                        noteTitle = noteTitle.substring(0,20)
                        noteTitle = noteTitle.substring(0,noteTitle.lastIndexOf(" "))
                    }
                    val noteOverview = NoteOverview(id= noteId,
                        title=noteTitle,
                        strongEncryption = false,
                        updatedOn = LocalDateTime.now().format(formatter))
                    selectedNotes.add(noteOverview)
                }
            }
            Log.v(TAG, "selectedNotes size $selectedNotes")
        }
    }

    fun importNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            selectedNotes.forEach{ note ->
                Log.v(TAG, "importing ${note.title}")
                val noteContent = fileRepository.readFile(Uri.parse(note.id))
                Log.v(TAG, "encrypting $noteContent")
                val importCipher = EncryptionUtils.getImportEncryptCipher()
                val encryptedData = Base64.encodeToString(importCipher
                    .doFinal(noteContent.toByteArray()), Base64.NO_WRAP)
                val iv =  Base64.encodeToString(importCipher.iv, Base64.DEFAULT)
                val combinedData  = "$iv|$encryptedData"
                Log.v(TAG, "encrypted data $combinedData[${combinedData.length}]")
                val importedNote = NoteModel("",noteRepository)
                importedNote.save(note.title, combinedData, note.strongEncryption)
            }
        }
        navController.navigate("list/")
    }

}