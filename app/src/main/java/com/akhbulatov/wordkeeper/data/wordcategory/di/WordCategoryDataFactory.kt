package com.akhbulatov.wordkeeper.data.wordcategory.di

import com.akhbulatov.wordkeeper.data.DataFactory
import com.akhbulatov.wordkeeper.data.word.di.WordDataFactory
import com.akhbulatov.wordkeeper.data.wordcategory.WordCategoryRepositoryImpl
import com.akhbulatov.wordkeeper.data.wordcategory.database.WordCategoryDao
import com.akhbulatov.wordkeeper.data.wordcategory.database.WordCategoryDatabaseMapper
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository

class WordCategoryDataFactory(
    private val dataFactory: DataFactory,
    private val wordDataFactory: WordDataFactory // TODO: This is wrong!
) {

    private val wordCategoryDao: WordCategoryDao by lazy {
        dataFactory.appDatabase.wordCategoryDao()
    }

    private val wordCategoryDatabaseMapper = WordCategoryDatabaseMapper()

    val wordCategoryRepository: WordCategoryRepository by lazy {
        WordCategoryRepositoryImpl(
            wordCategoryDao = wordCategoryDao,
            wordDao = wordDataFactory.wordDao,
            wordCategoryDatabaseMapper = wordCategoryDatabaseMapper,
            wordDatabaseMapper = wordDataFactory.wordDatabaseMapper
        )
    }
}
