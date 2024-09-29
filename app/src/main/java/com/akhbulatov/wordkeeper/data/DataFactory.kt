package com.akhbulatov.wordkeeper.data

import android.content.Context

class DataFactory(
    private val context: Context
) {

    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }
}
