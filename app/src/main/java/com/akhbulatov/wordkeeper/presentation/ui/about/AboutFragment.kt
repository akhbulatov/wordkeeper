package com.akhbulatov.wordkeeper.presentation.ui.about

import android.os.Bundle
import android.view.View
import com.akhbulatov.wordkeeper.BuildConfig
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.FragmentAboutBinding
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)

        val appVersion = String.format(
            getString(R.string.about_app_version),
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
        binding.appVersionTextView.text = appVersion
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}