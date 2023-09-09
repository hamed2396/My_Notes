package com.example.mynotes.ui.home

import android.util.Log
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.data.repository.home.HomeRepository
import com.example.mynotes.utils.applySchedulers
import com.example.mynotes.utils.base.BasePresenterImpl
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val view: HomeContract.View,
    private val repository: HomeRepository
) :
    BasePresenterImpl(), HomeContract.Presenter {
    override fun getNotes() {
        disposable = repository.getAllNotes().applySchedulers().subscribe {
            if (it.isNotEmpty()) {
                view.showNotes(it)
                view.showEmpty(false)
            } else {
                view.showEmpty(true)
            }

        }
    }


    override fun deleteNote(entity: NoteEntity) {
        disposable = repository.deleteNote(entity).subscribe {
            view.noteDeleteMessage()
        }
    }

    override fun searchNote(title: String) {
        disposable = repository.searchNote(title).applySchedulers().subscribe {
            if (it.isNotEmpty()) {
                view.showNotesFromSearch(it)
                view.emptySearch(false)
            } else {
                view.emptySearch(true)
            }
        }
    }

    override fun filterByDate() {
        disposable = repository.filterNoteByDate().applySchedulers().subscribe {
            if (it.isNotEmpty()) {

                view.showNotes(it)
            } else {
                view.showEmpty(true)
            }
        }
    }

    override fun filterAlphabetically() {
        disposable = repository.filterAlphabetically().applySchedulers().subscribe {
            if (it.isNotEmpty()) {

                view.showNotes(it)
            } else {
                view.showEmpty(true)
            }
        }
    }
}