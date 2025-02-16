package com.mynote.mynotes.file

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class FileRepository(private val applicationContext: Context) {
    suspend fun readFile(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            applicationContext.contentResolver.openInputStream(uri)?.let { inputStream ->
                InputStreamReader(inputStream).readText()
            }.toString()
        }
    }
}

/*applicationContext.assets.open(path).use { inputStream ->
             inputStream.readBytes().toString(Charsets.UTF_8)
         }*/