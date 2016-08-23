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
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.WordRecyclerViewAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.ui.widget.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.ui.widget.DividerItemDecoration;

/**
 * Displays a list of words that is updated when data changes in the database.
 * Loader uses a custom class for working with the database, not the ContentProvider
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;

    // The row ID in the database for the selected item in the word list
    private long mRowId;

    private static int mSortMode;

    private ContextMenuRecyclerView mWordListRecycler;
    private WordRecyclerViewAdapter mWordRecyclerAdapter;
    private DatabaseAdapter mDbAdapter;
    private TextView textEmptyWordList;
    protected FloatingActionButton fabAddWord;

    private WordClickListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WordClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement WordClickListener");
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
        mSortMode = mPrefs.getInt(WordSortDialogFragment.PREF_SORT_MODE, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        mWordListRecycler = (ContextMenuRecyclerView) rootView.findViewById(R.id.recycler_word_list);
        mWordListRecycler.setHasFixedSize(true);
        mWordListRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        mWordListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerForContextMenu(mWordListRecycler);

        fabAddWord = (FloatingActionButton) rootView.findViewById(R.id.fab_add_word);
        fabAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onWordClick(R.string.title_add_word,
                        R.string.action_add,
                        R.string.action_cancel);
            }
        });

        textEmptyWordList = (TextView) rootView.findViewById(R.id.text_empty_word_list);

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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_word_action, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info =
                (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_edit_word:
                // Saves the ID to use to retrieve the selected row
                // and paste the edited string into the database
                mRowId = info.id;

                mListener.onWordClick(R.string.title_edit_word,
                        R.string.action_edit_word,
                        R.string.action_cancel);
                return true;
            case R.id.menu_delete_word:
                deleteWord(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Returns a cursor with all records from the database.
        // Using own class instead of a ContentProvider
        return new SimpleCursorLoader(getActivity(), mDbAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mWordRecyclerAdapter == null) {
            // The adapter is created only the first time retrieving data from the database
            mWordRecyclerAdapter = new WordRecyclerViewAdapter(data);
            mWordRecyclerAdapter.setHasStableIds(true);
            mWordListRecycler.setAdapter(mWordRecyclerAdapter);
        } else {
            mWordRecyclerAdapter.swapCursor(data);
        }

        if (mWordRecyclerAdapter.getItemCount() == 0) {
            textEmptyWordList.setVisibility(View.VISIBLE);
        } else if (textEmptyWordList.getVisibility() == View.VISIBLE) {
            textEmptyWordList.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mWordRecyclerAdapter != null) {
            mWordRecyclerAdapter.swapCursor(null);
        }
    }

    public void changeSortWordList(int sortMode) {
        mSortMode = sortMode;
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
        mWordListRecycler.scrollToPosition(0);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    public boolean editWord(String name, String translation) {
        if (name.isEmpty() & translation.isEmpty() | (name.isEmpty() | translation.isEmpty())) {
            Toast.makeText(getActivity(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        mDbAdapter.updateRecord(mRowId, name, translation);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    private boolean deleteWord(long rowId) {
        if (!mDbAdapter.deleteRecord(rowId)) {
            Toast.makeText(getActivity(), R.string.error_delete_word, Toast.LENGTH_SHORT).show();
            return false;
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return true;
    }

    public String getName() {
        Cursor cursor = mDbAdapter.fetchRecord(mRowId);

        if (cursor.getCount() > 0) {
            return cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME));
        }
        return null;
    }

    public String getTranslation() {
        Cursor cursor = mDbAdapter.fetchRecord(mRowId);

        if (cursor.getCount() > 0) {
            return cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION));
        }
        return null;
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
            return mDbAdapter.fetchAllRecords(mSortMode);
        }
    }

    /**
     * Used to work with Dialog. Call the methods of the Activity in the Fragment
     */
    public interface WordClickListener {
        /**
         * Invoked on the event display dialog box
         *
         * @param titleId        ID of the title of the dialog
         * @param positiveTextId ID of the text on the positive button of the dialog
         * @param negativeTextId ID of the text on the negative button of the dialog
         */
        void onWordClick(int titleId, int positiveTextId, int negativeTextId);
    }
}
