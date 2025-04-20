package com.mynote.mynotes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDao {

    @Upsert
    fun save(note: NoteEntity)

    @Upsert
    fun save(tag: TagEntity): Long

    @Delete
    fun delete(tag: TagEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: String): NoteEntity

    @Query("SELECT * from notes ORDER BY updatedOn DESC")
    fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * from notes WHERE UPPER(title) LIKE UPPER(:searchText) ORDER BY updatedOn DESC")
    fun getAllNotes(searchText: String): List<NoteEntity>

    @Query("""
        SELECT Notes.id, Notes.title, Notes.content, Notes.updatedOn, Notes.strongEncryption
        FROM Notes
        LEFT JOIN NoteTagCrossRef ON Notes.id = NoteTagCrossRef.noteId
        LEFT JOIN Tags ON NoteTagCrossRef.tagId = Tags.id
        WHERE 
        (
            UPPER(Notes.title) LIKE UPPER(:searchText)
            OR UPPER(Tags.name) LIKE UPPER(:searchText)
        )
        AND Notes.updatedOn BETWEEN :fromDate AND :toDate
        ORDER BY Notes.updatedOn DESC;
    """)
    fun searchNotes(searchText: String, fromDate: Long, toDate: Long): List<NoteEntity>

    @Query("""
        SELECT * from notes
        WHERE UPDATEDON BETWEEN :fromDate AND :toDate
        ORDER BY updatedOn DESC
    """)
    fun searchNotes(fromDate: Long, toDate: Long): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteTagCrossRef(noteTagCrossRef: NoteTagCrossRef)

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId AND tagId IN (:tagIds)")
    fun deleteNoteTagCrossRef(noteId: String, tagIds: List<Long>)

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    fun deleteNoteTagCrossRef(noteId: String)

    @Query("SELECT * FROM tags WHERE name LIKE UPPER(:query) ORDER BY name")
    fun searchTags(query: String): List<TagEntity>

    @Query("""
        SELECT * FROM notes INNER JOIN NoteTagCrossRef ON
        notes.id = NoteTagCrossRef.noteId 
        WHERE NoteTagCrossRef.tagId = :tagId
    """)
    fun getNotesByTag(tagId: Long): List<NoteEntity>

    @Query("SELECT * from tags WHERE name = :name")
    fun getTag(name: String): TagEntity?

    @Query("""
            SELECT * FROM tags INNER JOIN NoteTagCrossRef ON
            tags.id = NoteTagCrossRef.tagId 
            WHERE NoteTagCrossRef.noteId = :noteId
    """)
    fun getTagsForNote(noteId: String): List<TagEntity>?

}