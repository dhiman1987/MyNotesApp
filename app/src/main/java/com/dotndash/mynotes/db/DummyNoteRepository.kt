package com.dotndash.mynotes.db

class DummyNoteRepository : NoteRepository {
    override fun getAll(): List<NoteEntity> {
        TODO("Not yet implemented")
    }

    override fun getAll(searchText: String): List<NoteEntity> {
        TODO("Not yet implemented")
    }

    override fun get(id: String): NoteEntity? {
        TODO("Not yet implemented")
    }

    override fun save(note: NoteEntity) {
        TODO("Not yet implemented")
    }

    override fun save(note: NoteEntity, tags: List<TagEntity>) {
        TODO("Not yet implemented")
    }

    override fun remove(note: NoteEntity) {
        TODO("Not yet implemented")
    }

    override fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity> {
        TODO("Not yet implemented")
    }

    override fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity> {
        TODO("Not yet implemented")
    }

    override fun getTagsForNote(noteId: String): List<TagEntity>? {
        TODO("Not yet implemented")
    }
}