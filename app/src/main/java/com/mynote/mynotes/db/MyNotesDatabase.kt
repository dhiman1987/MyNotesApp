package com.mynote.mynotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class MyNotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NoteDao

    companion object{
        @Volatile
        private var Instance: MyNotesDatabase? = null

        fun getDatabase(context: Context): MyNotesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyNotesDatabase::class.java, "my_notes_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}