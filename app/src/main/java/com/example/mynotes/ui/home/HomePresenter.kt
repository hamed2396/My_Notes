package com.example.mynotes.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.data.repository.home.HomeRepository
import com.example.mynotes.ui.MainActivity
import com.example.mynotes.utils.UserDataStore
import com.example.mynotes.utils.applySchedulers
import com.example.mynotes.utils.base.BasePresenterImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override fun getFilteredNote(fragment: Fragment) {
        disposable = (fragment.activity as MainActivity).filterNoteSubject.subscribe {
            view.showFilteredNote(it)
        }
    }

    override fun getLayoutManager(context: Context,dataStore: UserDataStore): Observable<RecyclerView.LayoutManager> {
        var layoutManager: RecyclerView.LayoutManager
        val layoutManagerSubject = BehaviorSubject.createDefault<RecyclerView.LayoutManager>(
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        )
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readFromDs().collect {

                layoutManager = if (it) {

                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                } else {

                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

                }


                layoutManagerSubject.onNext(layoutManager)

            }
        }

        return layoutManagerSubject.hide()

    }

    override fun getSearchText(fragment: Fragment) :Observable<String>{
        var title=""
        disposable=(fragment.activity as MainActivity).searchViewSubject.applySchedulers().subscribe {
            title=it
            if (it.isNotEmpty()) {
                searchNote(it)

            } else {

               getNotes()

            }
        }
        return Observable.just(title)
    }
}