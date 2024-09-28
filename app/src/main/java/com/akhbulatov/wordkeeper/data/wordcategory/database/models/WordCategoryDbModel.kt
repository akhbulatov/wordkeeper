package com.akhbulatov.wordkeeper.data.wordcategory.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class WordCategoryDbModel(
    @ColumnInfo(name = "name") val name: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0
}
