package com.mynote.mynotes.db

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    override fun getAll(): List<NoteEntity> = noteDao.getAllNotes().map { note -> note.decrypt() }
    override fun get(id: String): NoteEntity = noteDao.getNote(id).decrypt()
    override fun save(note: NoteEntity) = noteDao.save(note.encrypt())
    override fun remove(note: NoteEntity) = noteDao.delete(note)
}