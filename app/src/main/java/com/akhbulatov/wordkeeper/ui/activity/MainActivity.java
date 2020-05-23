package com.akhbulatov.wordkeeper.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.akhbulatov.wordkeeper.App;
import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment;
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment;
import com.akhbulatov.wordkeeper.presentation.ui.main.MainViewModel;
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment;
import com.akhbulatov.wordkeeper.presentation.ui.wordcategories.WordCategoriesFragment;
import com.akhbulatov.wordkeeper.util.CommonUtils;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public class MainActivity extends AppCompatActivity {

    private static final String BUNDLE_SCREEN_TITLE = "BUNDLE_SCREEN_TITLE";

    private static final String WORDS_FRAGMENT_TAG = WordsFragment.class.getName();
    private static final String CATEGORY_LIST_FRAGMENT_TAG = WordCategoriesFragment.class.getName();
    private static final String ABOUT_FRAGMENT_TAG = AboutFragment.class.getName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private WordsFragment mWordsFragment;

    @Inject
    NavigatorHolder navigatorHolder;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    Navigator navigator = new SupportAppNavigator(this, R.id.layout_root_container);
    private MainViewModel viewModel;

    private BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.layout_root_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent
                .mainComponentFactory()
                .create()
                .inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        // Also sets Toolbar's navigation click listener to toggle the drawer when it is clicked
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        // Creates an animation of the hamburger icon
        // for opening and closing the drawer
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });

        if (savedInstanceState != null) {
            mWordsFragment = (WordsFragment) getSupportFragmentManager().findFragmentByTag(WORDS_FRAGMENT_TAG);
            setTitle(savedInstanceState.getString(BUNDLE_SCREEN_TITLE));
        } else {
            mWordsFragment = new WordsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_root_container, mWordsFragment, WORDS_FRAGMENT_TAG)
                    .commit();

            setTitle(R.string.words_title);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Syncs the toggle state whenever the screen is restored
        // or there is a configuration change (i.e screen rotation)
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Passes any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SCREEN_TITLE, getTitle().toString());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // Returns to the main fragment and shows it
            mWordsFragment = new WordsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_root_container, mWordsFragment, WORDS_FRAGMENT_TAG)
                    .commit();

            mNavigationView.setCheckedItem(R.id.menu_drawer_all_words);
            setTitle(R.string.words_title);
        } else {
            super.onBackPressed();
        }
    }

    private void selectDrawerItem(MenuItem item) {
        Class fragmentClass = null;
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_drawer_categories:
                fragmentClass = WordCategoriesFragment.class;
                break;
            case R.id.menu_drawer_rate_app:
                showRateApp();
                break;
            case R.id.menu_drawer_about:
                fragmentClass = AboutFragment.class;
//                viewModel.onAboutClicked();
                break;
            default:
                fragmentClass = WordsFragment.class;
        }

        // Block is executed only if the selected item is a fragment,
        // otherwise was the selected activity and the replacement fragment is not executed
        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragmentClass == WordsFragment.class) {
                getSupportFragmentManager().popBackStack();
                transaction.replace(R.id.layout_root_container, fragment, WORDS_FRAGMENT_TAG);
                mWordsFragment = (WordsFragment) fragment;
            } else if (fragmentClass == WordCategoriesFragment.class) {
                transaction.replace(R.id.layout_root_container, fragment, CATEGORY_LIST_FRAGMENT_TAG);
                transaction.addToBackStack(null);
            } else {
                transaction.replace(R.id.layout_root_container, fragment, ABOUT_FRAGMENT_TAG);
                transaction.addToBackStack(null);
            }
            transaction.commit();

            // Not required for activity. Only for a fragment
            item.setChecked(true);
            setTitle(item.getTitle());
        }

        mDrawerLayout.closeDrawers();
    }

    private void showRateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            CommonUtils.showToast(this, R.string.error_rate_app);
        }
    }
}
