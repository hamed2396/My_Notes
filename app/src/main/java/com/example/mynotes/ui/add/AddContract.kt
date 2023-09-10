package com.example.mynotes.ui.add

import androidx.fragment.app.Fragment
import com.example.mynotes.data.models.entity.NoteEntity

interface AddContract {
    interface View {
        fun noteSavedMessage()

        fun showSingleNote(entity: NoteEntity)


    }
    interface Presenter {
        fun saveNote(entity: NoteEntity)

        fun getSingleNote(id:Int)


    }
}