package com.akhbulatov.wordkeeper.presentation.ui.categorywords

import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [CategoryWordsModule::class])
@FragmentScope
interface CategoryWordsComponent {
    fun inject(fragment: CategoryWordsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): CategoryWordsComponent
    }
}
