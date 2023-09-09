package com.example.mynotes.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynotes.utils.Constants

@Entity(Constants.TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var colorHex: Int = 0


)