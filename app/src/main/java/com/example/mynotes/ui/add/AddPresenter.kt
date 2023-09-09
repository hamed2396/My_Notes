package com.example.mynotes.ui.add

import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.data.repository.add.AddRepository
import com.example.mynotes.utils.applySchedulers
import com.example.mynotes.utils.base.BasePresenterImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AddPresenter @Inject constructor(
    private val view: AddContract.View,
    private val repository: AddRepository
) : BasePresenterImpl(), AddContract.Presenter {
    override fun saveNote(entity: NoteEntity) {
        disposable = repository.saveNote(entity).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                view.noteSavedMessage()
            }
    }

    override fun getSingleNote(id: Int) {
        disposable=repository.getSingleNote(id).applySchedulers().subscribe {
            view.showSingleNote(it)
        }
    }

}