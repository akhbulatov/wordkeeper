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
import com.akhbulatov.wordkeeper.ui.dialog.WordEditorDialogFragment;
import com.akhbulatov.wordkeeper.ui.fragment.CategoryFragment;
import com.akhbulatov.wordkeeper.ui.fragment.WordFragment;
import com.akhbulatov.wordkeeper.ui.listener.FabAddWordListener;

/**
 * Provides navigation drawer to switch between screens
 */
public class MainActivity extends AppCompatActivity implements FabAddWordListener,
        WordEditorDialogFragment.WordEditorDialogListener {

    private static final String BUNDLE_SCREEN_TITLE = "BUNDLE_SCREEN_TITLE";

    private static final String WORD_FRAGMENT_TAG = WordFragment.class.getName();
    private static final String CATEGORY_FRAGMENT_TAG = CategoryFragment.class.getName();

    private static final String WORD_EDITOR_DIALOG_ID = WordEditorDialogFragment.class.getName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private WordFragment mWordFragment;
    private CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Also sets Toolbar's navigation click listener to toggle the drawer when it is clicked
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        // Creates an animation of the hamburger icon
        // for opening and closing the drawer
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });

        if (savedInstanceState != null) {
            mWordFragment = (WordFragment)
                    getSupportFragmentManager().findFragmentByTag(WORD_FRAGMENT_TAG);
            mCategoryFragment = (CategoryFragment)
                    getSupportFragmentManager().findFragmentByTag(CATEGORY_FRAGMENT_TAG);

            setTitle(savedInstanceState.getString(BUNDLE_SCREEN_TITLE));
        } else {
            mWordFragment = new WordFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_root_container, mWordFragment, WORD_FRAGMENT_TAG)
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Passes any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
            // Returns to the main fragment and shows it
            mWordFragment = new WordFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_root_container, mWordFragment, WORD_FRAGMENT_TAG)
                    .commit();

            mNavigationView.setCheckedItem(R.id.menu_drawer_all_words);
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
    public void onFabAddWordClick(int titleId, int positiveTextId, int negativeTextId) {
        showWordEditorDialog(titleId, positiveTextId, negativeTextId);
    }

    // Passes the ID of the text on the positive button
    // to determine which the dialog (word) button was pressed: add or edit
    @Override
    public void onFinishWordEditorDialog(DialogFragment dialog, int positiveTextId) {
        // Add the word
        if (positiveTextId == R.string.word_editor_action_add) {
            mWordFragment.addWord(dialog);

            // Updates the category list only from the screen "Categories"
            if (mCategoryFragment != null && mCategoryFragment.isVisible()) {
                mCategoryFragment.updateCategoryList();
            }
        } else {
            // Edit the word
            Dialog dialogView = dialog.getDialog();

            EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation =
                    (EditText) dialogView.findViewById(R.id.edit_word_translation);
            Spinner spinnerCategories = (Spinner) dialogView.findViewById(R.id.spinner_categories);

            String name = editName.getText().toString();
            String translation = editTranslation.getText().toString();
            String category = spinnerCategories.getSelectedItem().toString();

            mWordFragment.editWord(name, translation, category);
        }
    }

    private void selectDrawerItem(MenuItem item) {
        Class fragmentClass = null;
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_drawer_all_words:
                fragmentClass = WordFragment.class;
                break;
            case R.id.menu_drawer_categories:
                fragmentClass = CategoryFragment.class;
                break;
            case R.id.menu_drawer_rate_app:
                showRateApp();
                break;
            case R.id.menu_drawer_about:
                showAbout();
                break;
            default:
                fragmentClass = WordFragment.class;
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
            if (fragmentClass == WordFragment.class) {
                getSupportFragmentManager().popBackStack();
                transaction.replace(R.id.layout_root_container, fragment, WORD_FRAGMENT_TAG);
                mWordFragment = (WordFragment) fragment;
            } else {
                transaction.replace(R.id.layout_root_container, fragment, CATEGORY_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                mCategoryFragment = (CategoryFragment) fragment;
            }
            transaction.commit();

            // Not required for activity. Only for a fragment
            item.setChecked(true);
            setTitle(item.getTitle());
        }

        mDrawerLayout.closeDrawers();
    }

    private void showWordEditorDialog(int titleId, int positiveTextId, int negativeTextId) {
        DialogFragment dialog = WordEditorDialogFragment
                .newInstance(titleId, positiveTextId, negativeTextId);
        dialog.show(getSupportFragmentManager(), WORD_EDITOR_DIALOG_ID);
        // NOTE! If the method is not called, the app crashes
        getSupportFragmentManager().executePendingTransactions();

        Dialog dialogView = dialog.getDialog();
        Spinner spinnerCategories = (Spinner) dialogView.findViewById(R.id.spinner_categories);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, mWordFragment.getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        // Receives and shows data of the selected word to edit in the dialog
        // Data is the name, translation and category
        if (positiveTextId == R.string.word_editor_action_edit) {
            EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation =
                    (EditText) dialogView.findViewById(R.id.edit_word_translation);

            editName.setText(mWordFragment.getName());
            editTranslation.setText(mWordFragment.getTranslation());
            spinnerCategories.setSelection(adapter.getPosition(mWordFragment.getCategory()));
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