package com.akhbulatov.wordkeeper.features.wordcategory.categorywords

import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [CategoryWordsModule::class, ViewModelModule::class])
interface CategoryWordsComponent {
    fun inject(fragment: CategoryWordsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): CategoryWordsComponent
    }

    companion object {
        fun create(): CategoryWordsComponent = App.appComponent
            .categoryWordsComponentFactory()
            .create()
    }
}
