/*
 * Copyright 2017 Alidibir Akhbulatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhbulatov.wordkeeper.ui.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.event.WordEditEvent;
import com.akhbulatov.wordkeeper.ui.dialog.WordEditorDialog;
import com.akhbulatov.wordkeeper.ui.fragment.CategoryListFragment;
import com.akhbulatov.wordkeeper.ui.fragment.WordListFragment;
import com.akhbulatov.wordkeeper.ui.listener.FabAddWordListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Provides navigation drawer to switch between screens
 */
public class MainActivity extends AppCompatActivity implements FabAddWordListener {

    private static final String BUNDLE_SCREEN_TITLE = "BUNDLE_SCREEN_TITLE";

    private static final String WORD_LIST_FRAGMENT_TAG = WordListFragment.class.getName();
    private static final String CATEGORY_LIST_FRAGMENT_TAG = CategoryListFragment.class.getName();

    private static final String WORD_EDITOR_DIALOG_ID = WordEditorDialog.class.getName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private WordListFragment mWordListFragment;
    private CategoryListFragment mCategoryListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Also sets Toolbar's navigation click listener to toggle the drawer when it is clicked
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        // Creates an animation of the hamburger icon
        // for opening and closing the drawer
        drawerLayout.addDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });

        if (savedInstanceState != null) {
            mWordListFragment = (WordListFragment)
                    getSupportFragmentManager().findFragmentByTag(WORD_LIST_FRAGMENT_TAG);
            mCategoryListFragment = (CategoryListFragment)
                    getSupportFragmentManager().findFragmentByTag(CATEGORY_LIST_FRAGMENT_TAG);

            setTitle(savedInstanceState.getString(BUNDLE_SCREEN_TITLE));
        } else {
            mWordListFragment = new WordListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_root_container, mWordListFragment, WORD_LIST_FRAGMENT_TAG)
                    .commit();

            setTitle(R.string.title_all_words);
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Passes any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
            // Returns to the main fragment and shows it
            mWordListFragment = new WordListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_root_container, mWordListFragment, WORD_LIST_FRAGMENT_TAG)
                    .commit();

            navigationView.setCheckedItem(R.id.menu_drawer_all_words);
            setTitle(R.string.title_all_words);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SCREEN_TITLE, getTitle().toString());
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onFabAddWordClick(int titleId, int positiveTextId, int negativeTextId) {
        showWordEditorDialog(titleId, positiveTextId, negativeTextId);
    }

    @Subscribe
    public void onWordEditSelected(WordEditEvent event) {
        /*
        uses the ID of the text on the positive button
        to determine which the dialog (word) button was pressed: add or edit
        */
        if (event.getPositiveTextId() == R.string.word_editor_action_add) {  // add the word
            mWordListFragment.addWord(event.getDialog());

            // Updates the category list only from the screen "Categories"
            if (mCategoryListFragment != null && mCategoryListFragment.isVisible()) {
                mCategoryListFragment.updateCategoryList();
            }
        } else {  // edit the word
            Dialog dialogView = event.getDialog().getDialog();

            EditText editName = dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation = dialogView.findViewById(R.id.edit_word_translation);
            Spinner spinnerCategories = dialogView.findViewById(R.id.spinner_categories);

            String name = editName.getText().toString();
            String translation = editTranslation.getText().toString();
            String category = spinnerCategories.getSelectedItem().toString();

            mWordListFragment.editWord(name, translation, category);
        }
    }

    private void selectDrawerItem(MenuItem item) {
        Class fragmentClass = null;
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_drawer_all_words:
                fragmentClass = WordListFragment.class;
                break;
            case R.id.menu_drawer_categories:
                fragmentClass = CategoryListFragment.class;
                break;
            case R.id.menu_drawer_rate_app:
                showRateApp();
                break;
            case R.id.menu_drawer_about:
                showAbout();
                break;
            default:
                fragmentClass = WordListFragment.class;
        }

        // Block is executed only if the selected item is a fragment,
        // otherwise was the selected activity and the replacement fragment is not executed
        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragmentClass == WordListFragment.class) {
                getSupportFragmentManager().popBackStack();
                transaction.replace(R.id.layout_root_container, fragment, WORD_LIST_FRAGMENT_TAG);
                mWordListFragment = (WordListFragment) fragment;
            } else {
                transaction.replace(R.id.layout_root_container, fragment, CATEGORY_LIST_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                mCategoryListFragment = (CategoryListFragment) fragment;
            }
            transaction.commit();

            // Not required for activity. Only for a fragment
            item.setChecked(true);
            setTitle(item.getTitle());
        }

        drawerLayout.closeDrawers();
    }

    private void showWordEditorDialog(int titleId, int positiveTextId, int negativeTextId) {
        DialogFragment dialog = WordEditorDialog.newInstance(titleId, positiveTextId, negativeTextId);
        dialog.show(getSupportFragmentManager(), WORD_EDITOR_DIALOG_ID);
        // NOTE! If the method is not called, the app crashes
        getSupportFragmentManager().executePendingTransactions();

        Dialog dialogView = dialog.getDialog();
        Spinner spinnerCategories = dialogView.findViewById(R.id.spinner_categories);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, mWordListFragment.getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        // Receives and shows data of the selected word to edit in the dialog
        // Data is the name, translation and category
        if (positiveTextId == R.string.word_editor_action_edit) {
            EditText editName = dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation = dialogView.findViewById(R.id.edit_word_translation);

            editName.setText(mWordListFragment.getName());
            editTranslation.setText(mWordListFragment.getTranslation());
            spinnerCategories.setSelection(adapter.getPosition(mWordListFragment.getCategory()));
        }
    }

    private void showRateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.error_rate_app, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
