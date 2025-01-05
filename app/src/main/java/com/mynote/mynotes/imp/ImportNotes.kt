package com.mynote.mynotes.imp

import java.nio.file.Files
import java.nio.file.Paths

fun ImportNote(filePath: String ){
    val path = Paths.get(filePath)
    if(!path.fileName.endsWith(".txt")){
        throw IllegalArgumentException("Supports only text files")
    }
    val noteTitle = path.fileName.toString().substring(0,(path.fileName.toString().length - 4))
    val noteContent  = Files.readAllLines(path)

}