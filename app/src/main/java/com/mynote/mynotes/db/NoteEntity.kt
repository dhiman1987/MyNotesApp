package com.mynote.mynotes.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mynote.mynotes.data.Note
import com.mynote.mynotes.data.NoteOverview
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val updatedOn: Long)

fun NoteEntity.toNote(): Note {
    val updatedOnDateTime = LocalDateTime
        .ofEpochSecond(this.updatedOn,0, ZoneOffset.UTC)
    this.updatedOn
    return Note(
        id=this.id,
        title=this.title,
        content=this.content,
        updatedOn = updatedOnDateTime)
}

fun NoteEntity.toNoteOverview(): NoteOverview {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    val updatedOnDateTime = LocalDateTime
        .ofEpochSecond(this.updatedOn,0, ZoneOffset.UTC)
    return NoteOverview(
        id=this.id,
        title=this.title,
        updatedOn = updatedOnDateTime.format(formatter))
}

fun NoteEntity.encrypt(): NoteEntity {
    return  NoteEntity(this.id,
        this.title,
        this.content,
        this.updatedOn)
}

fun NoteEntity.decrypt(): NoteEntity {
    return  NoteEntity(this.id,
        this.title,
        this.content,
        this.updatedOn)
}
