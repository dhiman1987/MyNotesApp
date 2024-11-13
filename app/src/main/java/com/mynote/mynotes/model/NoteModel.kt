package com.mynote.mynotes.model


import com.mynote.mynotes.data.Note
import com.mynote.mynotes.data.toNoteEntity
import com.mynote.mynotes.db.NoteRepository
import com.mynote.mynotes.db.toNote
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale.ENGLISH
import java.util.UUID

class NoteModel(noteId: String, private val noteRepository: NoteRepository) {

    private var note: Note = initNote(noteId)

    private fun initNote(noteId: String): Note {
        val date = LocalDate.now().dayOfMonth
        val defaultNote = Note(
            id = UUID.randomUUID().toString(),
            title = "${LocalDate.now().dayOfMonth} " +
                    "${LocalDate.now().month.getDisplayName(TextStyle.SHORT, ENGLISH)} " +
                    "note",
            content = "",
            updatedOn = LocalDateTime.now())
        if(noteId.isBlank()){
            return defaultNote;
        }
        val noteEntity = noteRepository.get(noteId)
        return if (null != noteEntity && !noteEntity.equals("")) {
            noteEntity.toNote()
        } else {
            defaultNote
        }
    }

    fun save(title:String?, content: String?): Note {
        if(title!=null){
            this.note = this.note.copy(title=title)
        }
        if(content!=null){
            this.note = this.note.copy(content=content)
        }
        this.note = this.note.copy(updatedOn = LocalDateTime.now())
        noteRepository.save(note.toNoteEntity())
        return this.note
    }

    fun get(): Note {
        return note
    }


    suspend fun remove(){
        noteRepository.remove(note.toNoteEntity())
    }

}