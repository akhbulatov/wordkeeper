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
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.CategoryAdapter;
import com.akhbulatov.wordkeeper.adapter.WordAdapter;
import com.akhbulatov.wordkeeper.database.CategoryDatabaseAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.database.WordDatabaseAdapter;
import com.akhbulatov.wordkeeper.ui.listener.FabAddWordListener;
import com.akhbulatov.wordkeeper.ui.widget.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.ui.widget.DividerItemDecoration;

/**
 * Shows a list of categories from the database.
 * Loader uses a custom class for working with the database,
 * NOT the ContentProvider (temporary solution)
 */
public class CategoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CategoryEditorDialogFragment.CategoryEditorDialogListener,
        CategoryDeleteDialogFragment.CategoryDeleteListener {

    private static final int LOADER_ID = 1;

    private static final int CATEGORY_EDITOR_DIALOG_REQUEST = 1;
    private static final int CATEGORY_DELETE_DIALOG_REQUEST = 2;

    private static final String CATEGORY_EDITOR_DIALOG_ID =
            CategoryEditorDialogFragment.class.getName();
    private static final String CATEGORY_DELETE_DIALOG_ID =
            CategoryDeleteDialogFragment.class.getName();

    // Contains the ID of the current selected item (category)
    private long mSelectedItemId;

    private ContextMenuRecyclerView mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private CategoryDatabaseAdapter mCategoryDbAdapter;
    private WordDatabaseAdapter mWordDbAdapter;

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

        mCategoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
        mCategoryDbAdapter.open();

        mWordDbAdapter = new WordDatabaseAdapter(getActivity());
        mWordDbAdapter.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategoryList = (ContextMenuRecyclerView)
                view.findViewById(R.id.recycler_category_list);
        mCategoryList.setHasFixedSize(true);
        mCategoryList.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mCategoryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerForContextMenu(mCategoryList);

        FloatingActionButton fabAddWord = (FloatingActionButton) view.findViewById(R.id.fab_add_word);
        fabAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFabAddWordClick(R.string.title_new_word,
                        R.string.word_editor_action_add,
                        R.string.word_editor_action_cancel);
            }
        });
    }

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
        inflater.inflate(R.menu.fragment_category_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_category:
                showCategoryEditorDialog(R.string.title_new_category,
                        R.string.category_editor_action_add,
                        R.string.category_editor_action_cancel);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.selected_category, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info =
                (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_rename_category:
                mSelectedItemId = info.id;
                showCategoryEditorDialog(R.string.title_rename_category,
                        R.string.category_editor_action_rename,
                        R.string.category_editor_action_cancel);
                return true;
            case R.id.menu_delete_category:
                mSelectedItemId = info.id;
                showCategoryDeleteDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Returns the cursor with all records from the database.
        // Uses own class instead of a ContentProvider
        return new SimpleCursorLoader(getActivity(), mCategoryDbAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mCategoryAdapter == null) {
            // The adapter is created only the first time retrieving data from the database
            mCategoryAdapter = new CategoryAdapter(getActivity(), data, mWordDbAdapter);
            mCategoryAdapter.setHasStableIds(true);
            mCategoryList.setAdapter(mCategoryAdapter);
        } else {
            mCategoryAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mCategoryAdapter != null) {
            mCategoryAdapter.swapCursor(null);
        }
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
            EditText editName = (EditText) dialogView.findViewById(R.id.edit_category_name);
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

    public void updateCategoryList() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void addCategory(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();

        EditText editName = (EditText) dialogView.findViewById(R.id.edit_category_name);
        String name = editName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(),
                    R.string.error_category_editor_empty_field,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            mCategoryDbAdapter.addRecord(name);
            mCategoryList.scrollToPosition(0);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void renameCategory(String name) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(),
                    R.string.error_category_editor_empty_field,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            // First updates all words from the category with the new category name
            Cursor cursor = mWordDbAdapter.fetchRecordsByCategory(getName());

            for (int i = 0; i < cursor.getCount(); i++) {
                int wordId = cursor.getInt(cursor.getColumnIndex(WordEntry._ID));
                String wordName =
                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME));
                String wordTranslation =
                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION));

                mWordDbAdapter.updateRecord(wordId, wordName, wordTranslation, name);
                cursor.moveToNext();
            }

            mCategoryDbAdapter.updateRecord(mSelectedItemId, name);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void deleteCategory() {
        // First, deletes all words that are in the deleted category
        Cursor cursor = mWordDbAdapter.fetchRecordsByCategory(getName());
        WordAdapter wordAdapter = new WordAdapter(getActivity(), cursor);

        for (int i = 0; i < cursor.getCount(); i++) {
            mWordDbAdapter.deleteRecord(wordAdapter.getItemId(cursor.getPosition()));
            cursor.moveToNext();
        }

        mCategoryDbAdapter.deleteRecord(mSelectedItemId);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Nullable
    private String getName() {
        Cursor cursor = mCategoryDbAdapter.fetchRecord(mSelectedItemId);
        if (cursor.getCount() > 0) {
            return cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        }
        return null;
    }

    private void showCategoryEditorDialog(int titleId, int positiveTextId, int negativeTextId) {
        DialogFragment dialog =
                CategoryEditorDialogFragment.newInstance(titleId, positiveTextId, negativeTextId);
        dialog.setTargetFragment(CategoryListFragment.this, CATEGORY_EDITOR_DIALOG_REQUEST);
        dialog.show(getActivity().getSupportFragmentManager(), CATEGORY_EDITOR_DIALOG_ID);

        // Receives and shows data of the selected category to edit in the dialog
        // Data is the name of the category
        if (positiveTextId == R.string.category_editor_action_rename) {
            // NOTE! If the method is not called, the app crashes
            getActivity().getSupportFragmentManager().executePendingTransactions();

            Dialog dialogView = dialog.getDialog();
            EditText editName = (EditText) dialogView.findViewById(R.id.edit_category_name);
            editName.setText(getName());
        }
    }

    private void showCategoryDeleteDialog() {
        DialogFragment dialog = new CategoryDeleteDialogFragment();
        dialog.setTargetFragment(CategoryListFragment.this, CATEGORY_DELETE_DIALOG_REQUEST);
        dialog.show(getActivity().getSupportFragmentManager(), CATEGORY_DELETE_DIALOG_ID);
    }

    /**
     * Used to work with a Loader instead of a ContentProvider
     */
    private static class SimpleCursorLoader extends CursorLoader {

        private CategoryDatabaseAdapter mCategoryDbAdapter;

        public SimpleCursorLoader(Context context, CategoryDatabaseAdapter сategoryDbAdapter) {
            super(context);
            mCategoryDbAdapter = сategoryDbAdapter;
        }

        @Override
        public Cursor loadInBackground() {
            return mCategoryDbAdapter.fetchAllRecords();
        }
    }
}
