package com.mynote.mynotes.data

import com.mynote.mynotes.db.NoteEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Note(val id: String, val title: String, val content: String, val updatedOn: LocalDateTime)

fun Note.toNoteEntity(): NoteEntity {
    val updatedOnLong = this.updatedOn.toEpochSecond(ZoneOffset.UTC)
    this.updatedOn
    return NoteEntity(
        id=this.id,
        title=this.title,
        content=this.content,
        updatedOn = updatedOnLong)
}
