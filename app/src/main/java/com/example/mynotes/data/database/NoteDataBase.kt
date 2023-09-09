package com.example.mynotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynotes.data.models.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDataBase:RoomDatabase() {
    abstract fun noteDao():NoteDao
}