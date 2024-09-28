package com.akhbulatov.wordkeeper.features.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.BuildConfig
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.ui.base.BaseActivity
import com.akhbulatov.wordkeeper.databinding.ActivityMainBinding
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject lateinit var navigatorHolder: NavigatorHolder
    private val navigator: Navigator by lazy {
        object : AppNavigator(this, R.id.container) {
            override fun setupFragmentTransaction(
                screen: FragmentScreen,
                fragmentTransaction: FragmentTransaction,
                currentFragment: Fragment?,
                nextFragment: Fragment
            ) {
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val drawerToggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.msg_drawer_open,
            R.string.msg_drawer_close
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MainComponent.create().inject(this)
        super.onCreate(savedInstanceState)
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        _binding = ActivityMainBinding.bind(rootView)
        setSupportActionBar(binding.toolbar)
        drawerToggle.setToolbarNavigationClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.drawerLayout.addDrawerListener(drawerToggle)
        binding.navigationView.setNavigationItemSelectedListener {
            onNavigationItemSelected(it)
            true
        }

        supportFragmentManager.addOnBackStackChangedListener {
            refreshActionBarWithNavDrawer()
        }

        if (savedInstanceState == null) {
            viewModel.onStart()
        } else {
            refreshActionBarWithNavDrawer()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun refreshActionBarWithNavDrawer() {
        val hasBackStack = supportFragmentManager.backStackEntryCount > 0
        drawerToggle.isDrawerIndicatorEnabled = !hasBackStack
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackStack)
        if (!hasBackStack) {
            drawerToggle.syncState()
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_drawer_words -> viewModel.onWordsClicked()
            R.id.menu_drawer_word_categories -> viewModel.onWordCategoriesClicked()
            R.id.menu_drawer_rate_app -> viewModel.onRateAppClicked(APP_PLAY_MARKET_URL)
            R.id.menu_drawer_about -> viewModel.onAboutClicked()
        }
        binding.drawerLayout.closeDrawers()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        private const val APP_PLAY_MARKET_URL = "market://details?id=${BuildConfig.APPLICATION_ID}"
    }
}
