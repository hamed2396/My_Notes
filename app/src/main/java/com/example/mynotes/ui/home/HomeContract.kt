package com.example.mynotes.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.utils.UserDataStore
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface HomeContract {
    interface View {
        fun showNotes(notes: List<NoteEntity>)
        fun showNotesFromSearch(notes: List<NoteEntity>)
        fun showEmpty(isEmpty: Boolean)
        fun noteDeleteMessage()
        fun emptySearch(empty: Boolean)
        fun showFilteredNote(id:Int)



    }

    interface Presenter {
        fun getNotes()
        fun deleteNote(entity: NoteEntity)
        fun searchNote(title: String)
        fun filterByDate()
        fun filterAlphabetically()
        fun getFilteredNote(fragment: Fragment)
        fun getLayoutManager(context: Context, dataStore: UserDataStore): Observable<LayoutManager>
        fun getSearchText(fragment: Fragment):Observable<String>
    }
}