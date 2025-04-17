package com.mynote.mynotes.model


import android.util.Log
import com.mynote.mynotes.data.Note
import com.mynote.mynotes.data.Tag
import com.mynote.mynotes.data.toNoteEntity
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.db.TagEntity
import com.mynote.mynotes.db.toNote
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale.ENGLISH
import java.util.UUID

class NoteModel(noteId: String, private val noteRepository: NoteRepository) {

    private val TAG = "NoteModel"
    private var note: Note = initNote(noteId)

    private fun initNote(noteId: String): Note {
        val defaultNote = Note(
            id = UUID.randomUUID().toString(),
            title = "${LocalDate.now().dayOfMonth} " +
                    "${LocalDate.now().month.getDisplayName(TextStyle.SHORT, ENGLISH)} " +
                    "note",
            content = """
                # Your Notes heading
                ## The subheading goes here
                Start writing your note.....
                You can write **BOLD texts**, __underlined text__ and *italic text*.
                Do not forget emojies. 😀🥰
                - Point one
                  - Sub point 1
                  - Sub point 2
                - Point 2
                - Pont 3 
               
            """.trimIndent(),
            strongEncryption = true,
            tags = null,
            updatedOn = LocalDateTime.now())
        if(noteId.isBlank()){
            return defaultNote;
        }
        val noteEntity = noteRepository.get(noteId)
        if (null != noteEntity && !noteEntity.equals("")) {
            val note = noteEntity.toNote()
            Log.v(TAG,"Fetching tags")
            val tags = noteRepository.getTagsForNote(noteId)
            if(tags!=null){
                Log.v(TAG,"Fetched tags ${tags.size}")
                note.tags = tags.map { tag -> Tag(tag.id,tag.name) }
            } else {
                Log.v(TAG,"no tags found")
            }
            return note
        } else {
            return defaultNote
        }
    }

    fun save(title:String?, content: String?, strongEncryption: Boolean?, tags: List<Tag>?): Note {
        if(title!=null){
            this.note = this.note.copy(title=title)
        }
        if(content!=null){
            this.note = this.note.copy(content=content)
        }
        if(strongEncryption!=null){
            this.note = this.note.copy(strongEncryption = strongEncryption)
        }
        this.note = this.note.copy(updatedOn = LocalDateTime.now())
        if(!tags.isNullOrEmpty()){
            val tagEntities = tags.map { tag -> TagEntity(tag.id,tag.name) }
            noteRepository.save(note.toNoteEntity(),tagEntities)
            note.tags = tags
        } else {
            noteRepository.save(note.toNoteEntity())
        }
        return this.note
    }

    fun get(): Note {
        return note
    }


    suspend fun remove(){
        noteRepository.remove(note.toNoteEntity())
    }

}