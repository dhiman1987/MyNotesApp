package com.mynote.mynotes.db

import android.util.Log

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    override fun getAll(): List<NoteEntity> = noteDao.getAllNotes()
    override fun getAll(searchText: String): List<NoteEntity> = noteDao.getAllNotes(searchText)
    override fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity> = noteDao.searchNotes(searchText,fromDate,toDate)
    override fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity> = noteDao.searchNotes(fromDate,toDate)
    override fun get(id: String): NoteEntity = noteDao.getNote(id)
    override fun save(note: NoteEntity) = noteDao.save(note)
    override fun remove(note: NoteEntity) = noteDao.delete(note)
}