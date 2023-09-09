package com.example.mynotes.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


}

fun <T : Any> Observable<T>.applySchedulersToClicks(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun View.showSnackBar(message: String, length: Int = Snackbar.LENGTH_SHORT,anchorView: View?=null){
    Snackbar.make(this,message,length).setAnchorView(anchorView).show()

}