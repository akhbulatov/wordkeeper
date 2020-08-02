package com.akhbulatov.wordkeeper.presentation.global.navigation

import android.content.Intent
import androidx.core.net.toUri
import com.github.terrakok.cicerone.androidx.ActivityScreen

object ExternalScreens {
    fun externalBrowser(url: String) = ActivityScreen("ExternalBrowser") {
        Intent(Intent.ACTION_VIEW, url.toUri())
    }
}
