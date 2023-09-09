package com.example.mynotes.data.repository.home

import com.example.mynotes.data.database.NoteDao
import com.example.mynotes.data.models.entity.NoteEntity
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dao: NoteDao) {
    fun getAllNotes() = dao.getAllNotes()
    fun deleteNote(entity: NoteEntity)= dao.deleteNote(entity)
    fun searchNote(title:String)= dao.searchNote(title)
    fun filterNoteByDate()= dao.filterByDateAdded()
    fun filterAlphabetically()= dao.filterAlphabetically()
}