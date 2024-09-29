package com.akhbulatov.wordkeeper.data.word.di

import com.akhbulatov.wordkeeper.core.di.CoreFactory
import com.akhbulatov.wordkeeper.data.DataFactory
import com.akhbulatov.wordkeeper.data.word.WordRepositoryImpl
import com.akhbulatov.wordkeeper.data.word.database.WordDao
import com.akhbulatov.wordkeeper.data.word.database.WordDatabaseMapper
import com.akhbulatov.wordkeeper.domain.word.WordRepository

class WordDataFactory(
    private val coreFactory: CoreFactory,
    private val dataFactory: DataFactory
) {

    val wordDao: WordDao by lazy { // TODO: Need to be private
        dataFactory.appDatabase.wordDao()
    }

    val wordDatabaseMapper = WordDatabaseMapper() // TODO: Need to be private

    val wordRepository: WordRepository by lazy {
        WordRepositoryImpl(
            wordDao = wordDao,
            appPreferences = coreFactory.preferencesFactory.appPreferences,
            wordDatabaseMapper = wordDatabaseMapper
        )
    }
}
