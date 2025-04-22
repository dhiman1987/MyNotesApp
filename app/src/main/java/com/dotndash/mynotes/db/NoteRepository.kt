package com.dotndash.mynotes.db

interface NoteRepository {
    fun getAll(): List<NoteEntity>
    fun getAll(searchText:String): List<NoteEntity>
    fun get(id: String): NoteEntity?
    fun save(note: NoteEntity)
    fun save(note: NoteEntity, tags: List<TagEntity>)
    fun remove(note: NoteEntity)
    fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity>
    fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity>
    fun getTagsForNote(noteId: String): List<TagEntity>?
}