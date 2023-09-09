package com.example.mynotes.utils.di

import android.content.Context
import androidx.room.Room
import com.example.mynotes.data.database.NoteDataBase
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NoteDataBase::class.java, Constants.DATABASE_NAME)
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(db:NoteDataBase) =db.noteDao()

    @Provides
    @Singleton
    fun provideEntity() = NoteEntity()
}