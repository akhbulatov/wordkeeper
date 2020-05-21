package com.akhbulatov.wordkeeper.data.global.local.database.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordDbModel(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "translation") val translation: String,
    @ColumnInfo(name = "datetime") val datetime: Long,
    @ColumnInfo(name = "category") val category: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0
}