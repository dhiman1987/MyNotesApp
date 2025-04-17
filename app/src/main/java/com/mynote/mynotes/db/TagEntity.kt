package com.mynote.mynotes.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
)

@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    @ColumnInfo(name = "noteId") val noteId: String,
    @ColumnInfo(name = "tagId") val tagId: Long
)

data class NoteWithTags(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags: List<TagEntity>
)