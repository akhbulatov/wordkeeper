package com.akhbulatov.wordkeeper.di

import android.content.Context
import com.akhbulatov.wordkeeper.di.modules.AppModule
import com.akhbulatov.wordkeeper.di.modules.DataModule
import com.akhbulatov.wordkeeper.di.modules.DatabaseModule
import com.akhbulatov.wordkeeper.di.modules.NavigationModule
import com.akhbulatov.wordkeeper.di.modules.PreferencesModule
import com.akhbulatov.wordkeeper.features.main.MainComponent
import com.akhbulatov.wordkeeper.features.word.addeditword.AddEditWordComponent
import com.akhbulatov.wordkeeper.features.word.words.WordsComponent
import com.akhbulatov.wordkeeper.features.wordcategory.categorywords.CategoryWordsComponent
import com.akhbulatov.wordkeeper.features.wordcategory.selectwordcategory.SelectWordCategoryComponent
import com.akhbulatov.wordkeeper.features.wordcategory.wordcategories.WordCategoriesComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        PreferencesModule::class,
        DataModule::class,
        NavigationModule::class
    ]
)
@Singleton
interface AppComponent {
    fun mainComponentFactory(): MainComponent.Factory
    fun wordsComponentFactory(): WordsComponent.Factory
    fun wordCategoriesComponentFactory(): WordCategoriesComponent.Factory
    fun categoryWordsComponentFactory(): CategoryWordsComponent.Factory
    fun addEditWordComponentFactory(): AddEditWordComponent.Factory
    fun selectWordCategoryComponentFactory(): SelectWordCategoryComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
