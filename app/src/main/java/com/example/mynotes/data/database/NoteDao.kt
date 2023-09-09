package com.example.mynotes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.utils.Constants
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface NoteDao {
    @Upsert
    fun upsertNote(entity: NoteEntity): Completable

    @Delete
    fun deleteNote(entity: NoteEntity): Completable

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAllNotes(): Observable<List<NoteEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE id ==:id")
    fun getSingleNote(id: Int): Observable<NoteEntity>

    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE title LIKE'%' || :title || '%'")
    fun searchNote(title: String): Observable<List<NoteEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME} ORDER BY title  ")
    fun filterAlphabetically(): Observable<List<NoteEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME} ORDER BY id DESC")
    fun filterByDateAdded(): Observable<List<NoteEntity>>
}