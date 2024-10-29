package com.mynote.mynotes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDao {

    @Upsert
    fun save(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: String): NoteEntity

    @Query("SELECT * from notes ORDER BY updatedOn DESC")
    fun getAllNotes(): List<NoteEntity>
}