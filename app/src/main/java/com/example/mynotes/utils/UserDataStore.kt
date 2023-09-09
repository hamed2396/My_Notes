package com.example.mynotes.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class UserDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    companion object{
        private val Context.dataStoreInfo by preferencesDataStore(Constants.DS_NAME)
        private val dataStoreKey = booleanPreferencesKey(Constants.KEY)
    }


    suspend fun writeOnDs(token: Boolean) {
        context.dataStoreInfo.edit {
            it[dataStoreKey] = token
        }
    }

     fun readFromDs() = context.dataStoreInfo.data.map {
        it[dataStoreKey] ?: false
    }
}