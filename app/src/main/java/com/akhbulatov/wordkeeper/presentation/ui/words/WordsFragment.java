package com.akhbulatov.wordkeeper.presentation.ui.words;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.akhbulatov.wordkeeper.App;
import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.databinding.FragmentWordsBinding;
import com.akhbulatov.wordkeeper.model.Word;
import com.akhbulatov.wordkeeper.presentation.ui.addeditword.AddEditWordDialog;
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment;
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordAdapter;
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordUiModel;
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordUiModelKt;
import com.akhbulatov.wordkeeper.presentation.ui.sortword.SortWordDialog;
import com.akhbulatov.wordkeeper.ui.activity.MainActivity;
import com.akhbulatov.wordkeeper.ui.dialog.CategoryListDialog;
import com.akhbulatov.wordkeeper.util.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

public class WordsFragment extends BaseFragment implements CategoryListDialog.CategoryListDialogListener {

    private static final int CATEGORY_LIST_DIALOG_REQUEST = 2;

    private WordAdapter mWordAdapter;

    private ActionModeCallback mActionModeCallback;
    private ActionMode mActionMode;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private WordsViewModel viewModel;

    private FragmentWordsBinding binding;

    public WordsFragment() {
        super(R.layout.fragment_words);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent
                .wordsComponentFactory()
                .create()
                .inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(WordsViewModel.class);
        viewModel.loadWords();

        mWordAdapter = new WordAdapter(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull com.akhbulatov.wordkeeper.domain.global.models.Word word, int position) {
                if (mActionMode != null) {
                    toggleSelection(position);
                }
            }

            @Override
            public boolean onItemLongClick(@NotNull com.akhbulatov.wordkeeper.domain.global.models.Word word, int position) {
                if (mActionMode == null) {
                    mActionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(mActionModeCallback);
                }
                toggleSelection(position);
                return true;
            }
        });

        mActionModeCallback = new ActionModeCallback();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentWordsBinding.bind(view);
        binding.wordsRecyclerView.setHasFixedSize(true);
        binding.wordsRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.wordsRecyclerView.setAdapter(mWordAdapter);
        binding.addWordFab.setOnClickListener(v -> showAddEditWordDialog(null));

        viewModel.getViewState().observe(getViewLifecycleOwner(), this::renderViewState);
    }

    @Override
    public void onDestroyView() {
        binding.wordsRecyclerView.setAdapter(null);
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_word, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_word);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(new ComponentName(requireActivity(), MainActivity.class)));
        }
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                final Cursor cursor = mWordDbAdapter.getAll(sSortMode);
//                final int column = cursor.getColumnIndex(WordEntry.COLUMN_NAME);
                if (newText.length() > 0) {
//                    mWordAdapter.swapCursor(new FilterCursorWrapper(cursor, newText, column));

                    binding.emptyDataTextView.setVisibility(View.GONE);
                    if (mWordAdapter.getItemCount() == 0) {
                        String escapedNewText = TextUtils.htmlEncode(newText);
                        String formattedNoResults = String.format(
                                getString(R.string.words_no_results), escapedNewText);
                        CharSequence styledNoResults = Html.fromHtml(formattedNoResults);

                        binding.noResultsTextView.setText(styledNoResults);
                        binding.noResultsTextView.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultsTextView.setVisibility(View.GONE);
                    }
                } else {
//                    mWordAdapter.swapCursor(cursor);

                    binding.noResultsTextView.setVisibility(View.GONE);
                    if (mWordAdapter.getItemCount() == 0) {
                        binding.noResultsTextView.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sort_word) {
            showWordSortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void renderViewState(WordsViewModel.ViewState viewState) {
        showEmptyProgress(viewState.getEmptyProgress());
        showEmptyData(viewState.getEmptyData());
        showEmptyError(viewState.getEmptyError().getFirst(), viewState.getEmptyError().getSecond());
        showWords(viewState.getWords().getFirst(), viewState.getWords().getSecond());
    }

    private void showEmptyProgress(boolean show) {
        binding.emptyProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyData(boolean show) {
        binding.emptyDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyError(boolean show, String message) {
        binding.errorTextView.setText(message);
        binding.errorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWords(boolean show, List<com.akhbulatov.wordkeeper.domain.global.models.Word> words) {
        mWordAdapter.submitList(words);
        binding.wordsRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showAddEditWordDialog(com.akhbulatov.wordkeeper.domain.global.models.Word word) {
        WordUiModel uiModel = null;
        if (word != null) {
            uiModel = WordUiModelKt.toUiModel(word);
        }
        AddEditWordDialog.newInstance(uiModel).show(getParentFragmentManager(), null);
    }

    // Updates the word list with the new sort mode
    @Override
    public void onFinishCategoryListDialog(String category) {
        Word word = null;
        for (Integer i : mWordAdapter.getSelectedWords()) {
//            word = mWordDbAdapter.get(mWordAdapter.getItemId(i));
//            mWordDbAdapter.update(new Word(
//                    word.getId(),
//                    word.getName(),
//                    word.getTranslation(),
//                    category));
        }

        mActionMode.finish();

        if (word != null) {
            CommonUtils.showToast(getActivity(), R.string.words_success_move);
        } else {
            CommonUtils.showToast(getActivity(), R.string.words_error_move);
        }
    }

//    public String[] getCategories() {
//        CategoryDatabaseAdapter categoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
//        categoryDbAdapter.open();
//
//        Cursor cursor = categoryDbAdapter.getAll();
//        List<Category> categoryList = Category.getCategories(cursor);
//        String[] categories = new String[categoryList.size()];
//        for (int i = 0; i < categoryList.size(); i++) {
//            categories[i] = categoryList.get(i).getName();
//        }
//
//        cursor.close();
//        categoryDbAdapter.close();
//        return categories;
//    }

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

    private void showWordSortDialog() {
        getParentFragmentManager().setFragmentResultListener(
                SortWordDialog.REQUEST_SORT_MODE,
                getViewLifecycleOwner(),
                (requestKey, result) -> viewModel.loadWords()
        );
        DialogFragment dialog = new SortWordDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }

    private void showCategoryListDialog() {
        DialogFragment dialog = new CategoryListDialog();
        dialog.setTargetFragment(WordsFragment.this, CATEGORY_LIST_DIALOG_REQUEST);
        dialog.show(requireActivity().getSupportFragmentManager(), null);
    }

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
                    int position = mWordAdapter.getSelectedWords().get(0);
                    com.akhbulatov.wordkeeper.domain.global.models.Word word = mWordAdapter.getCurrentList().get(position);
                    showAddEditWordDialog(word);
                    mode.finish();
                    return true;
                case R.id.menu_delete_word:
                    List<com.akhbulatov.wordkeeper.domain.global.models.Word> words = new ArrayList<>();
                    for (int pos : mWordAdapter.getSelectedWords()) {
                        words.add(mWordAdapter.getCurrentList().get(pos));
                    }
                    viewModel.onDeleteWordsClicked(words);
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