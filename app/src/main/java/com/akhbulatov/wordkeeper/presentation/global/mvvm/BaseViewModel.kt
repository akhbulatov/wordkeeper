package com.akhbulatov.wordkeeper.presentation.global.mvvm

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun onBackPressed()
}