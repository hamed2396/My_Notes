package com.example.mynotes.ui.home

import com.example.mynotes.data.models.entity.NoteEntity

interface HomeContract {
    interface View {
        fun showNotes(notes: List<NoteEntity>)
        fun showNotesFromSearch(notes: List<NoteEntity>)
        fun showEmpty(isEmpty: Boolean)
        fun noteDeleteMessage()
        fun emptySearch(empty: Boolean)
    }

    interface Presenter {
        fun getNotes()
        fun deleteNote(entity: NoteEntity)
        fun searchNote(title: String)
        fun filterByDate()
        fun filterAlphabetically()
    }
}