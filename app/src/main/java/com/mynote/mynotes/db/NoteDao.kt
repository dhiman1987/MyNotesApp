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

    @Query("SELECT * from notes WHERE UPPER(title) LIKE UPPER(:searchText) ORDER BY updatedOn DESC")
    fun getAllNotes(searchText: String): List<NoteEntity>

    @Query("SELECT * from notes " +
            "WHERE UPPER(title) LIKE UPPER(:searchText) " +
            "AND UPDATEDON BETWEEN :fromDate AND :toDate " +
            "ORDER BY updatedOn DESC")
    fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity>

    @Query("SELECT * from notes " +
            "WHERE UPDATEDON BETWEEN :fromDate AND :toDate " +
            "ORDER BY updatedOn DESC")
    fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity>
}