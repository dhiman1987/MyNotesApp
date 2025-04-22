package com.dotndash.mynotes.db

import android.util.Log

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    private val TAG = "NoteRepositoryImpl"
    override fun getAll(): List<NoteEntity> = noteDao.getAllNotes()
    override fun getAll(searchText: String): List<NoteEntity> = noteDao.getAllNotes(searchText)
    override fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity> = noteDao.searchNotes(searchText,fromDate,toDate)
    override fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity> = noteDao.searchNotes(fromDate,toDate)
    override fun get(id: String): NoteEntity = noteDao.getNote(id)
    override fun save(note: NoteEntity) = noteDao.save(note)
    override fun save(note: NoteEntity, tags: List<TagEntity>) {
        noteDao.save(note)
        val noteId = note.id
        noteDao.deleteNoteTagCrossRef(noteId)
        Log.v(TAG,"saving Tags ${tags.size}")
        tags.forEach{ tag ->
            var tagId = tag.id
            Log.v(TAG,"Checking tag $tagId - ${tag.name}")
            val existingTag = noteDao.getTag(tag.name)
            if(null == existingTag){
                tagId = noteDao.save(tag)
                Log.v(TAG,"New tag inserted $tagId - ${tag.name}")
            } else {
                tagId = existingTag.id
                Log.v(TAG,"Existing tag found $tagId - ${existingTag.name}")
            }
            Log.v(TAG,"Saving mapping $noteId - $tagId")
            noteDao.insertNoteTagCrossRef(NoteTagCrossRef(noteId = noteId, tagId = tagId))
        }
    }
    override fun remove(note: NoteEntity) {
        noteDao.deleteNoteTagCrossRef(note.id)
        noteDao.delete(note)
    }

    override fun getTagsForNote(noteId: String): List<TagEntity>? =  noteDao.getTagsForNote(noteId)
}