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

package com.akhbulatov.wordkeeper.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.WordAdapter;
import com.akhbulatov.wordkeeper.database.CategoryDatabaseAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.database.WordDatabaseAdapter;
import com.akhbulatov.wordkeeper.event.CategoryEvent;
import com.akhbulatov.wordkeeper.event.SortEvent;
import com.akhbulatov.wordkeeper.model.Category;
import com.akhbulatov.wordkeeper.model.Word;
import com.akhbulatov.wordkeeper.ui.activity.MainActivity;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryListDialog;
import com.akhbulatov.wordkeeper.ui.dialog.WordSortDialog;
import com.akhbulatov.wordkeeper.ui.listener.FabAddWordListener;
import com.akhbulatov.wordkeeper.util.FilterCursorWrapper;
import com.akhbulatov.wordkeeper.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Shows a list of words from the database.
 * Loader uses a custom class for working with the database,
 * NOT the ContentProvider (temporary solution)
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        WordAdapter.WordViewHolder.WordAdapterListener {

    private static final int LOADER_ID = 1;

    private static final int WORD_SORT_DIALOG_REQUEST = 1;
    private static final int CATEGORY_LIST_DIALOG_REQUEST = 2;

    private static final String WORD_SORT_DIALOG_ID = WordSortDialog.class.getName();
    private static final String CATEGORY_LIST_DIALOG_ID = CategoryListDialog.class.getName();

    private static int sSortMode;

    // Contains the ID of the current selected item (word)
    private long mSelectedItemId;

    @BindView(R.id.recycler_word_list)
    RecyclerView wordList;
    @BindView(R.id.text_empty_word_list)
    TextView textEmptyWordList;
    @BindView(R.id.text_no_results_word)
    TextView textNoResultsWord;

    private Unbinder mUnbinder;
    private WordAdapter mWordAdapter;
    private WordDatabaseAdapter mWordDbAdapter;

    private ActionModeCallback mActionModeCallback;
    private ActionMode mActionMode;

    private FabAddWordListener mListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FabAddWordListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + FabAddWordListener.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mWordDbAdapter = new WordDatabaseAdapter(getActivity());
        mWordDbAdapter.open();

        sSortMode = SharedPreferencesManager.getSortMode(getActivity());

        mActionModeCallback = new ActionModeCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        wordList.setHasFixedSize(true);
        wordList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        wordList.setLayoutManager(new LinearLayoutManager(getActivity()));

        textEmptyWordList.setVisibility(View.GONE);
        textNoResultsWord.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWordDbAdapter.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_word, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_word);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(new ComponentName(getActivity(), MainActivity.class)));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final Cursor cursor = mWordDbAdapter.getAll(sSortMode);
                final int column = cursor.getColumnIndex(WordEntry.COLUMN_NAME);
                if (newText.length() > 0) {
                    mWordAdapter.swapCursor(new FilterCursorWrapper(cursor, newText, column));

                    textEmptyWordList.setVisibility(View.GONE);
                    if (mWordAdapter.getItemCount() == 0) {
                        String escapedNewText = TextUtils.htmlEncode(newText);
                        String formattedNoResults = String.format(
                                getString(R.string.no_results_word), escapedNewText);
                        CharSequence styledNoResults = Html.fromHtml(formattedNoResults);

                        textNoResultsWord.setText(styledNoResults);
                        textNoResultsWord.setVisibility(View.VISIBLE);
                    } else {
                        textNoResultsWord.setVisibility(View.GONE);
                    }
                } else {
                    mWordAdapter.swapCursor(cursor);

                    textNoResultsWord.setVisibility(View.GONE);
                    if (mWordAdapter.getItemCount() == 0) {
                        textEmptyWordList.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_word:
                showWordSortDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Returns the cursor with all records from the database.
        // Uses own class instead of a ContentProvider
        return new SimpleCursorLoader(getActivity(), mWordDbAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mWordAdapter == null) {
            // The adapter is created only the first time retrieving data from the database
            mWordAdapter = new WordAdapter(data, this);
            mWordAdapter.setHasStableIds(true);
            wordList.setAdapter(mWordAdapter);
        } else {
            mWordAdapter.swapCursor(data);
        }

        if (mWordAdapter.getItemCount() == 0) {
            textEmptyWordList.setVisibility(View.VISIBLE);
        } else {
            textEmptyWordList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mWordAdapter != null) {
            mWordAdapter.swapCursor(null);
        }
    }

    @Override
    public void onWordItemClick(int position) {
        if (mActionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onWordItemLongClick(int position) {
        if (mActionMode == null) {
            mActionMode = ((AppCompatActivity)
                    getActivity()).startSupportActionMode(mActionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    @OnClick(R.id.fab_add_word)
    public void onAddWordClicked() {
        mListener.onFabAddWordClick(R.string.title_new_word,
                R.string.word_editor_action_add,
                android.R.string.cancel);
    }

    // Updates the word list with the new sort mode
    @Subscribe
    public void onSortWordsSelected(SortEvent event) {
        // Saves to pass to the inner class SimpleCursorLoader
        sSortMode = event.getMode();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    // Moves the marked words in the selected category
    @Subscribe
    public void onMoveWordsSelected(CategoryEvent event) {
        Word word = null;
        for (Integer i : mWordAdapter.getSelectedWords()) {
            word = mWordDbAdapter.get(mWordAdapter.getItemId(i));
            mWordDbAdapter.update(new Word(
                    word.getId(),
                    word.getName(),
                    word.getTranslation(),
                    event.getName()));
        }

        mActionMode.finish();

        if (word != null) {
            Toast.makeText(getActivity(), R.string.success_move_word, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.error_move_word, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receives the entered data (word) and saves in the database
     *
     * @param dialog The dialog from where take the data (word) to save
     */
    public void addWord(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();

        EditText editName = (EditText) dialogView.findViewById(R.id.edit_word_name);
        EditText editTranslation = (EditText) dialogView.findViewById(R.id.edit_word_translation);
        Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.spinner_categories);

        String name = editName.getText().toString();
        String translation = editTranslation.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if ((TextUtils.isEmpty(name) & TextUtils.isEmpty(translation))
                | (TextUtils.isEmpty(name) | TextUtils.isEmpty(translation))) {
            Toast.makeText(getActivity(),
                    R.string.error_word_editor_empty_fields,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            mWordDbAdapter.insert(new Word(name, translation, category));
            // Checked for null in case this method is called from the screen "Categories"
            if (wordList != null) {
                wordList.scrollToPosition(0);
            }
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public void editWord(String name, String translation, String category) {
        if ((TextUtils.isEmpty(name) & TextUtils.isEmpty(translation))
                | (TextUtils.isEmpty(name) | TextUtils.isEmpty(translation))) {
            Toast.makeText(getActivity(),
                    R.string.error_word_editor_empty_fields,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            mWordDbAdapter.update(new Word(mSelectedItemId, name, translation, category));
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public String getName() {
        return mWordDbAdapter.get(mSelectedItemId).getName();
    }

    public String getTranslation() {
        return mWordDbAdapter.get(mSelectedItemId).getTranslation();
    }

    public String getCategory() {
        return mWordDbAdapter.get(mSelectedItemId).getCategory();
    }

    public String[] getCategories() {
        CategoryDatabaseAdapter categoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
        categoryDbAdapter.open();

        Cursor cursor = categoryDbAdapter.getAll();
        List<Category> categoryList = Category.getCategories(cursor);
        String[] categories = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            categories[i] = categoryList.get(i).getName();
        }

        cursor.close();
        categoryDbAdapter.close();
        return categories;
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

    private void deleteWords(List<Integer> words) {
        for (Integer i : words) {
            mWordDbAdapter.delete(new Word(mWordAdapter.getItemId(i)));
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * Gets the single item (word) ID from the list of words,
     * despite the collection that is passed in the parameter
     *
     * @param words The list of selected words
     * @return Returns the item (word) ID
     */
    private long getWordItemId(List<Integer> words) {
        long id = 0;
        for (Integer i : words) {
            id = mWordAdapter.getItemId(i);
        }
        return id;
    }

    private void showWordSortDialog() {
        DialogFragment dialog = new WordSortDialog();
        dialog.setTargetFragment(WordListFragment.this, WORD_SORT_DIALOG_REQUEST);
        dialog.show(getActivity().getSupportFragmentManager(), WORD_SORT_DIALOG_ID);
    }

    private void showCategoryListDialog() {
        DialogFragment dialog = new CategoryListDialog();
        dialog.setTargetFragment(WordListFragment.this, CATEGORY_LIST_DIALOG_REQUEST);
        dialog.show(getActivity().getSupportFragmentManager(), CATEGORY_LIST_DIALOG_ID);
    }

    /**
     * Used to work with a Loader instead of a ContentProvider
     */
    private static class SimpleCursorLoader extends CursorLoader {

        private WordDatabaseAdapter mWordDbAdapter;

        public SimpleCursorLoader(Context context, WordDatabaseAdapter wordDbAdapter) {
            super(context);
            mWordDbAdapter = wordDbAdapter;
        }

        @Override
        public Cursor loadInBackground() {
            return mWordDbAdapter.getAll(sSortMode);
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
                case R.id.menu_move_word:
                    showCategoryListDialog();
                    return true;
                case R.id.menu_edit_word:
                    // Saves the id to use to retrieve the selected row
                    // and paste the edited string into the database.
                    // Called for only one selected word
                    mSelectedItemId = getWordItemId(mWordAdapter.getSelectedWords());

                    mListener.onFabAddWordClick(R.string.title_edit_word,
                            R.string.word_editor_action_edit,
                            android.R.string.cancel);
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
}
