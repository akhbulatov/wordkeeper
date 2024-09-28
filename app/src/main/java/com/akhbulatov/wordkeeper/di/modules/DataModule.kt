package com.akhbulatov.wordkeeper.di.modules

import com.akhbulatov.wordkeeper.data.word.WordRepositoryImpl
import com.akhbulatov.wordkeeper.data.wordcategory.WordCategoryRepositoryImpl
import com.akhbulatov.wordkeeper.domain.global.repositories.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.word.WordRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindWordRepository(repository: WordRepositoryImpl): WordRepository

    @Binds
    @Singleton
    abstract fun bindWordCategoryRepository(repository: WordCategoryRepositoryImpl): WordCategoryRepository
}
