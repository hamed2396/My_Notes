package com.example.mynotes.data.repository.add

import com.example.mynotes.data.database.NoteDao
import com.example.mynotes.data.models.entity.NoteEntity
import javax.inject.Inject

class AddRepository @Inject constructor(private val dao: NoteDao) {
    fun saveNote(entity: NoteEntity)= dao.upsertNote(entity)
    fun getSingleNote(id:Int)= dao.getSingleNote(id)

}