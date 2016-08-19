/*
 * Copyright 2016 Alidibir Akhbulatov <alidibir.akhbulatov@gmail.com>
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

package com.akhbulatov.wordkeeper.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.akhbulatov.wordkeeper.R;

/**
 * Displays the main fragment.
 * Implements methods to display the editor dialog boxes of the word
 * and the context menu to perform actions on words
 */
public class MainActivity extends AppCompatActivity implements
        WordEditorDialogFragment.WordEditorDialogListener, WordListFragment.WordClickListener,
        WordSortDialogFragment.WordSortDialogListener {

    private static final String WORD_EDITOR_DIALOG_ID = WordEditorDialogFragment.class.getName();
    private static final String WORD_SORT_DIALOG_ID = WordSortDialogFragment.class.getName();

    private WordListFragment mWordListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        mWordListFragment = (WordListFragment)
                getFragmentManager().findFragmentById(R.id.fragment_word_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_word_sort:
                showWordSortDialog();
                return true;
            case R.id.menu_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Passes the ID of the text on the positive button
    // to determine which dialog button was pressed: dialog add a word or edit word
    @Override
    public void onPositiveClick(DialogFragment dialog, int positiveTextId) {
        // Add the word
        if (positiveTextId == R.string.action_add) {
            mWordListFragment.addWord(dialog);
        } else {
            // Edit the word
            Dialog dialogView = dialog.getDialog();

            EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation =
                    (EditText) dialogView.findViewById(R.id.edit_word_translation);
            String name = editName.getText().toString();
            String translation = editTranslation.getText().toString();

            mWordListFragment.editWord(name, translation);
        }
    }

    // Used as an interface to call a method from MainActivity in WordListFragment
    @Override
    public void onWordClick(int titleId, int positiveTextId, int negativeTextId) {
        showWordEditorDialog(titleId, positiveTextId, negativeTextId);

    }

    // Used as an interface for receiving selection events sorting mode
    @Override
    public void onSingleChoiceClick(int sortMode) {
        mWordListFragment.changeSortWordList(sortMode);
    }

    private void showWordEditorDialog(int titleId, int positiveTextId, int negativeTextId) {
        DialogFragment dialog = WordEditorDialogFragment
                .newInstance(titleId, positiveTextId, negativeTextId);
        dialog.show(getFragmentManager(), WORD_EDITOR_DIALOG_ID);

        // For edit word dialog receives and displays data of the selected word
        // Data is the name and translation
        if (positiveTextId == R.string.action_edit_word) {
            // NOTE! If the method is not called, the program crashes
            getFragmentManager().executePendingTransactions();

            Dialog dialogView = dialog.getDialog();

            EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
            EditText editTranslation =
                    (EditText) dialogView.findViewById(R.id.edit_word_translation);
            editName.setText(mWordListFragment.getName());
            editTranslation.setText(mWordListFragment.getTranslation());
        }
    }

    private void showWordSortDialog() {
        DialogFragment dialog = new WordSortDialogFragment();
        dialog.show(getFragmentManager(), WORD_SORT_DIALOG_ID);
    }

    private void showAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
