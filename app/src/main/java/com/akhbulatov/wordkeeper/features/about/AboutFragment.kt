package com.akhbulatov.wordkeeper.features.about

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.ui.base.BaseComposeFragment
import com.akhbulatov.wordkeeper.core.ui.utils.requireCompatActivity

class AboutFragment : BaseComposeFragment() {

    @Composable
    override fun ComposeContent() {
        AboutScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireCompatActivity().supportActionBar?.setTitle(R.string.about_title)
    }
}
