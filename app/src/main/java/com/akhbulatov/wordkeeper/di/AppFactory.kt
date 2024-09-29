package com.akhbulatov.wordkeeper.di

import android.content.Context
import com.akhbulatov.wordkeeper.core.di.CoreFactory
import com.akhbulatov.wordkeeper.data.DataFactory
import com.akhbulatov.wordkeeper.data.word.di.WordDataFactory
import com.akhbulatov.wordkeeper.data.wordcategory.di.WordCategoryDataFactory
import com.akhbulatov.wordkeeper.domain.word.di.WordDomainFactory
import com.akhbulatov.wordkeeper.domain.wordcategory.di.WordCategoryDomainFactory

class AppFactory(
    val appContext: Context
) {

    val navigationFactory: NavigationFactory by lazy {
        NavigationFactory()
    }
    private val coreFactory: CoreFactory by lazy {
        CoreFactory(
            sharedPrefs = appContext.getSharedPreferences("wordkeeper.prefs", Context.MODE_PRIVATE)
        )
    }
    private val dataFactory: DataFactory by lazy {
        DataFactory(
            context = appContext
        )
    }

    private val wordDataFactory: WordDataFactory by lazy {
        WordDataFactory(
            coreFactory = coreFactory,
            dataFactory = dataFactory
        )
    }
    val wordDomainFactory: WordDomainFactory by lazy {
        WordDomainFactory(
            wordRepository = wordDataFactory.wordRepository
        )
    }

    private val wordCategoryDataFactory: WordCategoryDataFactory by lazy {
        WordCategoryDataFactory(
            dataFactory = dataFactory,
            wordDataFactory = wordDataFactory
        )
    }
    val wordCategoryDomainFactory: WordCategoryDomainFactory by lazy {
        WordCategoryDomainFactory(
            wordCategoryRepository = wordCategoryDataFactory.wordCategoryRepository
        )
    }
}
