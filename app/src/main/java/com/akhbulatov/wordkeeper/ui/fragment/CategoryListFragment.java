/*
 * Copyright 2018 Alidibir Akhbulatov
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

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.CategoryAdapter;
import com.akhbulatov.wordkeeper.adapter.WordAdapter;
import com.akhbulatov.wordkeeper.database.CategoryDatabaseAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.database.WordDatabaseAdapter;
import com.akhbulatov.wordkeeper.model.Category;
import com.akhbulatov.wordkeeper.model.Word;
import com.akhbulatov.wordkeeper.ui.activity.CategoryContentActivity;
import com.akhbulatov.wordkeeper.ui.activity.MainActivity;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryDeleteDialog;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryEditorDialog;
import com.akhbulatov.wordkeeper.ui.listener.FabAddWordListener;
import com.akhbulatov.wordkeeper.ui.widget.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.util.CommonUtils;
import com.akhbulatov.wordkeeper.util.FilterCursorWrapper;

/**
 * Shows a list of categories from the database.
 * Loader uses a custom class for working with the database,
 * NOT the ContentProvider (temporary solution)
 */
public class CategoryListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CategoryAdapter.CategoryItemClickListener,
        CategoryEditorDialog.CategoryEditorDialogListener,
        CategoryDeleteDialog.CategoryDeleteListener {

    private static final int LOADER_ID = 1;

    private static final int CATEGORY_EDITOR_DIALOG_REQUEST = 1;
    private static final int CATEGORY_DELETE_DIALOG_REQUEST = 2;

    // Contains the ID of the current selected item (category)
    private long mSelectedItemId;

    private ContextMenuRecyclerView mCategoryList;
    private TextView mTextNoResultsCategory;

    private CategoryAdapter mCategoryAdapter;
    private CategoryDatabaseAdapter mCategoryDbAdapter;
    private WordDatabaseAdapter mWordDbAdapter;

    private FabAddWordListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (FabAddWordListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement "
                    + FabAddWordListener.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCategoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
        mCategoryDbAdapter.open();

        mWordDbAdapter = new WordDatabaseAdapter(getActivity());
        mWordDbAdapter.open();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategoryList = view.findViewById(R.id.recycler_category_list);
        mCategoryList.setHasFixedSize(true);
        mCategoryList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mCategoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        registerForContextMenu(mCategoryList);

        mTextNoResultsCategory = view.findViewById(R.id.text_no_results_category);
        mTextNoResultsCategory.setVisibility(View.GONE);

        FloatingActionButton fabAddWord = view.findViewById(R.id.fab_add_word);
        fabAddWord.setOnClickListener(view1 ->
                mListener.onFabAddWordClick(R.string.title_new_word,
                        R.string.word_editor_action_add,
                        android.R.string.cancel));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCategoryDbAdapter.close();
        mWordDbAdapter.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_category);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(new ComponentName(requireContext(), MainActivity.class)));
        }
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                final Cursor cursor = mCategoryDbAdapter.getAll();
                final int column = cursor.getColumnIndex(CategoryEntry.COLUMN_NAME);
                if (newText.length() > 0) {
                    mCategoryAdapter.swapCursor(new FilterCursorWrapper(cursor, newText, column));

                    if (mCategoryAdapter.getItemCount() == 0) {
                        String escapedNewText = TextUtils.htmlEncode(newText);
                        String formattedNoResults = String.format(
                                getString(R.string.no_results_category), escapedNewText);
                        CharSequence styledNoResults = Html.fromHtml(formattedNoResults);

                        mTextNoResultsCategory.setText(styledNoResults);
                        mTextNoResultsCategory.setVisibility(View.VISIBLE);
                    } else {
                        mTextNoResultsCategory.setVisibility(View.GONE);
                    }
                } else {
                    mCategoryAdapter.swapCursor(cursor);
                    mTextNoResultsCategory.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_category:
                showCategoryEditorDialog(R.string.title_new_category, R.string.category_editor_action_add);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.selected_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info =
                (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_rename_category:
                mSelectedItemId = info.id;
                showCategoryEditorDialog(R.string.title_rename_category, R.string.category_editor_action_rename);
                return true;
            case R.id.menu_delete_category:
                mSelectedItemId = info.id;
                showCategoryDeleteDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Returns the cursor with all records from the database.
        // Uses own class instead of a ContentProvider
        return new SimpleCursorLoader(getActivity(), mCategoryDbAdapter);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (mCategoryAdapter == null) {
            // The adapter is created only the first time retrieving data from the database
            mCategoryAdapter = new CategoryAdapter(data, mWordDbAdapter);
            mCategoryAdapter.setHasStableIds(true);
            mCategoryAdapter.setOnItemClickListener(this);
            mCategoryList.setAdapter(mCategoryAdapter);
        } else {
            mCategoryAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (mCategoryAdapter != null) {
            mCategoryAdapter.swapCursor(null);
        }
    }

    @Override
    public void onCategoryItemClick(String categoryName) {
        startActivity(CategoryContentActivity.newIntent(getActivity(), categoryName));
    }

    // Passes the ID of the text on the positive button
    // to determine which the dialog (category) button was pressed: add or edit
    @Override
    public void onFinishCategoryEditorDialog(DialogFragment dialog, int positiveTextId) {
        // Add the category
        if (positiveTextId == R.string.category_editor_action_add) {
            addCategory(dialog);
        } else {
            // Edit the category
            Dialog dialogView = dialog.getDialog();
            EditText editName = dialogView.findViewById(R.id.edit_category_name);
            String name = editName.getText().toString();

            renameCategory(name);
        }
    }

    // Confirms delete the category.
    // Also removed all words that are in the category
    @Override
    public void onFinishCategoryDeleteDialog(DialogFragment dialog) {
        deleteCategory();
    }

    @SuppressWarnings("deprecation")
    public void updateCategoryList() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @SuppressWarnings("deprecation")
    private void addCategory(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();

        EditText editName = dialogView.findViewById(R.id.edit_category_name);
        String name = editName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            CommonUtils.showToast(getActivity(), R.string.error_category_editor_empty_field);
        } else {
            mCategoryDbAdapter.insert(new Category(name));
            mCategoryList.scrollToPosition(0);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @SuppressWarnings("deprecation")
    private void renameCategory(String name) {
        if (TextUtils.isEmpty(name)) {
            CommonUtils.showToast(getActivity(), R.string.error_category_editor_empty_field);
        } else {
            // First updates all words from the category with the new category name
            Cursor cursor = mWordDbAdapter.getRecordsByCategory(getName());
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex(WordEntry._ID));
                String wordName =
                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME));
                String wordTranslation =
                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION));

                mWordDbAdapter.update(new Word(id, wordName, wordTranslation, name));
                cursor.moveToNext();
            }

            mCategoryDbAdapter.update(new Category(mSelectedItemId, name));
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @SuppressWarnings("deprecation")
    private void deleteCategory() {
        // First, deletes all words that are in the deleted category
        Cursor cursor = mWordDbAdapter.getRecordsByCategory(getName());
        WordAdapter wordAdapter = new WordAdapter(cursor);
        while (!cursor.isAfterLast()) {
            long id = wordAdapter.getItemId(cursor.getPosition());
            mWordDbAdapter.delete(new Word(id));
            cursor.moveToNext();
        }

        mCategoryDbAdapter.delete(new Category(mSelectedItemId));
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Nullable
    private String getName() {
        return mCategoryDbAdapter.get(mSelectedItemId).getName();
    }

    private void showCategoryEditorDialog(int titleId, int positiveTextId) {
        DialogFragment dialog = CategoryEditorDialog.newInstance(titleId, positiveTextId, android.R.string.cancel);
        dialog.setTargetFragment(CategoryListFragment.this, CATEGORY_EDITOR_DIALOG_REQUEST);
        dialog.show(requireActivity().getSupportFragmentManager(), null);

        // Receives and shows data of the selected category to edit in the dialog
        // Data is the name of the category
        if (positiveTextId == R.string.category_editor_action_rename) {
            // NOTE! If the method is not called, the app crashes
            requireActivity().getSupportFragmentManager().executePendingTransactions();

            Dialog dialogView = dialog.getDialog();
            EditText editName = dialogView.findViewById(R.id.edit_category_name);
            editName.setText(getName());
        }
    }

    private void showCategoryDeleteDialog() {
        DialogFragment dialog = new CategoryDeleteDialog();
        dialog.setTargetFragment(CategoryListFragment.this, CATEGORY_DELETE_DIALOG_REQUEST);
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }

    /**
     * Used to work with a Loader instead of a ContentProvider
     */
    private static class SimpleCursorLoader extends CursorLoader {

        private CategoryDatabaseAdapter mCategoryDbAdapter;

        SimpleCursorLoader(Context context, CategoryDatabaseAdapter categoryDbAdapter) {
            super(context);
            mCategoryDbAdapter = categoryDbAdapter;
        }

        @Override
        public Cursor loadInBackground() {
            return mCategoryDbAdapter.getAll();
        }
    }
}
