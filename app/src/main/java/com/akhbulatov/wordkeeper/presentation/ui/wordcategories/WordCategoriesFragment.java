package com.akhbulatov.wordkeeper.presentation.ui.wordcategories;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.App;
import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.databinding.FragmentWordCategoriesBinding;
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory;
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment;
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordCategoryAdapter;
import com.akhbulatov.wordkeeper.ui.activity.CategoryContentActivity;
import com.akhbulatov.wordkeeper.ui.activity.MainActivity;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryDeleteDialog;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryEditorDialog;
import com.akhbulatov.wordkeeper.ui.widget.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.util.CommonUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

public class WordCategoriesFragment extends BaseFragment implements
        CategoryEditorDialog.CategoryEditorDialogListener,
        CategoryDeleteDialog.CategoryDeleteListener {

    private static final int CATEGORY_EDITOR_DIALOG_REQUEST = 1;
    private static final int CATEGORY_DELETE_DIALOG_REQUEST = 2;

    // Contains the ID of the current selected item (category)
    private long mSelectedItemId;

    private ContextMenuRecyclerView mCategoryList;
    private TextView mTextNoResultsCategory;

    private WordCategoryAdapter mWordCategoryAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private WordCategoriesViewModel viewModel;

    private FragmentWordCategoriesBinding binding;

    public WordCategoriesFragment() {
        super(R.layout.fragment_word_categories);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent
                .wordCategoriesComponentFactory()
                .create()
                .inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(WordCategoriesViewModel.class);
        viewModel.loadWordCategories();

        mWordCategoryAdapter = new WordCategoryAdapter(wordCategory ->
                startActivity(CategoryContentActivity.newIntent(getActivity(), wordCategory.getName())));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentWordCategoriesBinding.bind(view);
        binding.wordCategoriesRecyclerView.setHasFixedSize(true);
        binding.wordCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.wordCategoriesRecyclerView.setAdapter(mWordCategoryAdapter);
        registerForContextMenu(binding.wordCategoriesRecyclerView);

        mTextNoResultsCategory = view.findViewById(R.id.noResultsTextView);
        mTextNoResultsCategory.setVisibility(View.GONE);

//        fabAddWord.setOnClickListener(view1 ->
//                mListener.onFabAddWordClick(R.string.add_edit_word_add_title,
//                        R.string.add_edit_word_action_add,
//                        android.R.string.cancel)
//        );

        viewModel.getViewState().observe(getViewLifecycleOwner(), this::renderViewState);
    }

    @Override
    public void onDestroyView() {
        binding.wordCategoriesRecyclerView.setAdapter(null);
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
//                final Cursor cursor = mCategoryDbAdapter.getAll();
//                final int column = cursor.getColumnIndex(CategoryEntry.COLUMN_NAME);
                if (newText.length() > 0) {
//                    mWordCategoryAdapter.swapCursor(new FilterCursorWrapper(cursor, newText, column));

                    if (mWordCategoryAdapter.getItemCount() == 0) {
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
//                    mWordCategoryAdapter.swapCursor(cursor);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_category) {
            showCategoryEditorDialog(R.string.title_new_category, R.string.category_editor_action_add);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.selected_category, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
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

    private void renderViewState(WordCategoriesViewModel.ViewState viewState) {
        showEmptyProgress(viewState.getEmptyProgress());
        showEmptyData(viewState.getEmptyData());
        showEmptyError(viewState.getEmptyError().getFirst(), viewState.getEmptyError().getSecond());
        showWordCategories(viewState.getWordCategories().getFirst(), viewState.getWordCategories().getSecond());
    }

    private void showEmptyProgress(boolean show) {
//        binding.emptyProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyData(boolean show) {
//        binding.emptyDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyError(boolean show, String message) {
//        binding.errorTextView.setText(message);
//        binding.errorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWordCategories(boolean show, List<WordCategory> wordCategories) {
        mWordCategoryAdapter.submitList(wordCategories);
        binding.wordCategoriesRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
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

    public void updateCategoryList() {
//        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    private void addCategory(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();

        EditText editName = dialogView.findViewById(R.id.edit_category_name);
        String name = editName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            CommonUtils.showToast(getActivity(), R.string.error_category_editor_empty_field);
        } else {
//            mCategoryDbAdapter.insert(new Category(name));
            mCategoryList.scrollToPosition(0);
//            loaderManager.restartLoader(LOADER_ID, null, this);
        }
    }

    private void renameCategory(String name) {
//        if (TextUtils.isEmpty(name)) {
//            CommonUtils.showToast(getActivity(), R.string.error_category_editor_empty_field);
//        } else {
//            // First updates all words from the category with the new category name
//            Cursor cursor = mWordDbAdapter.getRecordsByCategory(getName());
//            while (!cursor.isAfterLast()) {
//                long id = cursor.getLong(cursor.getColumnIndex(WordEntry._ID));
//                String wordName =
//                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME));
//                String wordTranslation =
//                        cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION));
//
//                mWordDbAdapter.update(new Word(id, wordName, wordTranslation, name));
//                cursor.moveToNext();
//            }
//
//            mCategoryDbAdapter.update(new Category(mSelectedItemId, name));
////            loaderManager.restartLoader(LOADER_ID, null, this);
//        }
    }

    private void deleteCategory() {
        // First, deletes all words that are in the deleted category
//        Cursor cursor = mWordDbAdapter.getRecordsByCategory(getName());
//        WordAdapter wordAdapter = new WordAdapter(cursor);
//        while (!cursor.isAfterLast()) {
//            long id = wordAdapter.getItemId(cursor.getPosition());
//            mWordDbAdapter.delete(new Word(id));
//            cursor.moveToNext();
//        }

//        mCategoryDbAdapter.delete(new Category(mSelectedItemId));
//        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    private String getName() {
//        return mCategoryDbAdapter.get(mSelectedItemId).getName();
        return "";
    }

    private void showCategoryEditorDialog(int titleId, int positiveTextId) {
        DialogFragment dialog = CategoryEditorDialog.newInstance(titleId, positiveTextId, android.R.string.cancel);
        dialog.setTargetFragment(WordCategoriesFragment.this, CATEGORY_EDITOR_DIALOG_REQUEST);
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
        dialog.setTargetFragment(WordCategoriesFragment.this, CATEGORY_DELETE_DIALOG_REQUEST);
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }
}
