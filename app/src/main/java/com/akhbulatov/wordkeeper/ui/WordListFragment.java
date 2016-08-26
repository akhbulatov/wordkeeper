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

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.WordAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.ui.widget.DividerItemDecoration;

import java.util.List;

/**
 * Displays a list of words that is updated when data changes in the database.
 * Loader uses a custom class for working with the database, not the ContentProvider
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        WordAdapter.WordViewHolder.WordClickListener {

    private static final int LOADER_ID = 0;

    // The word ID in the database for the selected item in the word list
    private long mWordId;

    private static int sSortMode;

    private RecyclerView mWordList;
    private WordAdapter mWordAdapter;
    private DatabaseAdapter mDbAdapter;
    private TextView mTextEmptyWordList;
    protected FloatingActionButton fabAddWord;

    private ActionModeCallback mActionModeCallback;
    private ActionMode mActionMode;

    private EditWordClickListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EditWordClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + EditWordClickListener.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbAdapter = new DatabaseAdapter(getActivity());
        mDbAdapter.open();

        SharedPreferences mPrefs = getActivity()
                .getSharedPreferences(WordSortDialogFragment.PREF_NAME, Context.MODE_PRIVATE);
        // If the sort mode is not set use default "Last modified" (value is 1)
        sSortMode = mPrefs.getInt(WordSortDialogFragment.PREF_SORT_MODE, 1);

        mActionModeCallback = new ActionModeCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        mWordList = (RecyclerView) rootView.findViewById(R.id.recycler_word_list);
        mWordList.setHasFixedSize(true);
        mWordList.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        mWordList.setLayoutManager(new LinearLayoutManager(getActivity()));

        fabAddWord = (FloatingActionButton) rootView.findViewById(R.id.fab_add_word);
        fabAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEditWordClick(R.string.title_add_word,
                        R.string.action_add,
                        R.string.action_cancel);
            }
        });

        mTextEmptyWordList = (TextView) rootView.findViewById(R.id.text_empty_word_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbAdapter.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Returns a cursor with all records from the database.
        // Using own class instead of a ContentProvider
        return new SimpleCursorLoader(getActivity(), mDbAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mWordAdapter == null) {
            // The adapter is created only the first time retrieving data from the database
            mWordAdapter = new WordAdapter(data, this);
            mWordAdapter.setHasStableIds(true);
            mWordList.setAdapter(mWordAdapter);
        } else {
            mWordAdapter.swapCursor(data);
        }

        if (mWordAdapter.getItemCount() == 0) {
            mTextEmptyWordList.setVisibility(View.VISIBLE);
        } else {
            mTextEmptyWordList.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mWordAdapter != null) {
            mWordAdapter.swapCursor(null);
        }
    }

    @Override
    public void onWordClick(int position) {
        if (mActionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onWordLongClick(int position) {
        if (mActionMode == null) {
            mActionMode = ((AppCompatActivity)
                    getActivity()).startSupportActionMode(mActionModeCallback);
        }

        toggleSelection(position);
        return true;
    }

    public void changeSortWordList(int sortMode) {
        sSortMode = sortMode;
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * Receives the entered data (word) and saves in the database
     *
     * @param dialog Dialog box from where take the data (word) to save
     * @return Returns true if word was added, otherwise returns false
     */
    public boolean addWord(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();

        EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
        EditText editTranslation = (EditText) dialogView.findViewById(R.id.edit_word_translation);
        String name = editName.getText().toString();
        String translation = editTranslation.getText().toString();

        if (name.isEmpty() & translation.isEmpty() | (name.isEmpty() | translation.isEmpty())) {
            Toast.makeText(getActivity(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }

        mDbAdapter.addRecord(name, translation);
        mWordList.scrollToPosition(0);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    public boolean editWord(String name, String translation) {
        if (name.isEmpty() & translation.isEmpty() | (name.isEmpty() | translation.isEmpty())) {
            Toast.makeText(getActivity(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }

        mDbAdapter.updateRecord(mWordId, name, translation);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    public String getName() {
        Cursor cursor = mDbAdapter.fetchRecord(mWordId);

        if (cursor.getCount() > 0) {
            return cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME));
        }
        return null;
    }

    public String getTranslation() {
        Cursor cursor = mDbAdapter.fetchRecord(mWordId);

        if (cursor.getCount() > 0) {
            return cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION));
        }
        return null;
    }

    private void toggleSelection(int position) {
        mWordAdapter.toggleSelection(position);
        int count = mWordAdapter.getSelectedWordCount();

        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(String.valueOf(count));
            mActionMode.invalidate();
        }
    }

    private boolean deleteWords(List<Integer> words) {
        for (Integer i : words) {
            mDbAdapter.deleteRecord(mWordAdapter.getItemId(i));
        }

        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    /**
     * Gets a single item id from the list of words,
     * despite the collection that is passed in the parameter
     *
     * @param words List of selected words
     * @return Returns the item id
     */
    private long getWordId(List<Integer> words) {
        long wordId = 0;
        for (Integer i : words) {
            wordId = mWordAdapter.getItemId(i);
        }
        return wordId;
    }

    /**
     * Used to work with a Loader instead of a ContentProvider
     */
    private static class SimpleCursorLoader extends CursorLoader {

        private DatabaseAdapter mDbAdapter;

        public SimpleCursorLoader(Context context, DatabaseAdapter dbAdapter) {
            super(context);
            mDbAdapter = dbAdapter;
        }

        @Override
        public Cursor loadInBackground() {
            return mDbAdapter.fetchAllRecords(sSortMode);
        }
    }

    /**
     * Provides support for CAB
     */
    private class ActionModeCallback implements ActionMode.Callback {

        private MenuItem mItemEditWord;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.selected_word, menu);
            mItemEditWord = menu.findItem(R.id.menu_edit_word);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Editing is available only for one selected word
            if (mWordAdapter.getSelectedWordCount() == 1) {
                mItemEditWord.setVisible(true);
            } else {
                mItemEditWord.setVisible(false);
            }
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_edit_word:
                    // Saves the id to use to retrieve the selected row
                    // and paste the edited string into the database.
                    // Called for only one selected word
                    mWordId = getWordId(mWordAdapter.getSelectedWords());

                    mListener.onEditWordClick(R.string.title_edit_word,
                            R.string.action_edit_word,
                            R.string.action_cancel);
                    mode.finish();
                    return true;
                case R.id.menu_delete_word:
                    deleteWords(mWordAdapter.getSelectedWords());
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mWordAdapter.clearSelection();
            mActionMode = null;
        }
    }

    /**
     * Used to work with Dialog. Call the methods of the Activity in the Fragment
     */
    public interface EditWordClickListener {
        /**
         * Invoked on the event display dialog box
         *
         * @param titleId        ID of the title of the dialog
         * @param positiveTextId ID of the text on the positive button of the dialog
         * @param negativeTextId ID of the text on the negative button of the dialog
         */
        void onEditWordClick(int titleId, int positiveTextId, int negativeTextId);
    }
}
