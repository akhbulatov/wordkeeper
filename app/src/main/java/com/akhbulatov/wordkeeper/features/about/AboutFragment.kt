package com.akhbulatov.wordkeeper.features.about

import android.os.Bundle
import android.view.View
import com.akhbulatov.wordkeeper.BuildConfig
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.ui.base.BaseFragment
import com.akhbulatov.wordkeeper.core.ui.utils.requireCompatActivity
import com.akhbulatov.wordkeeper.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAboutBinding.bind(view)
        with(binding) {
            requireCompatActivity().supportActionBar?.setTitle(R.string.about_title)

            val appVersion = String.format(
                getString(R.string.about_app_version),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE
            )
            appVersionTextView.text = appVersion
        }
    }
}
