package com.akhbulatov.wordkeeper.di

import android.content.Context
import com.akhbulatov.wordkeeper.di.modules.AppModule
import com.akhbulatov.wordkeeper.di.modules.DataModule
import com.akhbulatov.wordkeeper.di.modules.DatabaseModule
import com.akhbulatov.wordkeeper.di.modules.NavigationModule
import com.akhbulatov.wordkeeper.di.modules.PreferencesModule
import com.akhbulatov.wordkeeper.di.modules.ViewModelModule
import com.akhbulatov.wordkeeper.presentation.ui.addeditword.AddEditWordComponent
import com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory.AddEditWordCategoryComponent
import com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory.DeleteWordCategoryComponent
import com.akhbulatov.wordkeeper.presentation.ui.main.di.MainComponent
import com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory.SelectWordCategoryComponent
import com.akhbulatov.wordkeeper.presentation.ui.sortword.SortWordComponent
import com.akhbulatov.wordkeeper.presentation.ui.wordcategories.WordCategoriesComponent
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        PreferencesModule::class,
        DataModule::class,
        NavigationModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {
    fun mainComponentFactory(): MainComponent.Factory
    fun wordsComponentFactory(): WordsComponent.Factory
    fun wordCategoriesComponentFactory(): WordCategoriesComponent.Factory
    fun addEditWordComponentFactory(): AddEditWordComponent.Factory
    fun sortWordComponentFactory(): SortWordComponent.Factory
    fun addEditWordCategoryComponentFactory(): AddEditWordCategoryComponent.Factory
    fun deleteWordCategoryComponentFactory(): DeleteWordCategoryComponent.Factory
    fun selectWordCategoryComponentFactory(): SelectWordCategoryComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}