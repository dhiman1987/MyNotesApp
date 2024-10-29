package com.mynote.mynotes.db

interface NoteRepository {
    fun getAll(): List<NoteEntity>
    fun get(id: String): NoteEntity?
    fun save(note: NoteEntity)
    fun remove(note: NoteEntity)
}