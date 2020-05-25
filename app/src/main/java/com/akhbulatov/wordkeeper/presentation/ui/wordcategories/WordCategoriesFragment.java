package com.akhbulatov.wordkeeper.presentation.ui.wordcategories;

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

import com.akhbulatov.wordkeeper.App;
import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.databinding.FragmentWordCategoriesBinding;
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory;
import com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory.AddEditWordCategoryDialog;
import com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory.DeleteWordCategoryDialog;
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment;
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordCategoryAdapter;
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel;
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModelKt;
import com.akhbulatov.wordkeeper.presentation.ui.global.views.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.ui.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

public class WordCategoriesFragment extends BaseFragment {

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

        mWordCategoryAdapter = new WordCategoryAdapter(new WordCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull WordCategory wordCategory) {
                viewModel.onWordCategoryClicked(wordCategory);
            }

            @Override
            public void onMoreOptionsClick(@NotNull View view) {
                view.showContextMenu();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentWordCategoriesBinding.bind(view);
        binding.wordCategoriesRecyclerView.setHasFixedSize(true);
        binding.wordCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.wordCategoriesRecyclerView.setAdapter(mWordCategoryAdapter);
        registerForContextMenu(binding.wordCategoriesRecyclerView);

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

                        binding.noResultsTextView.setText(styledNoResults);
                        binding.noResultsTextView.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultsTextView.setVisibility(View.GONE);
                    }
                } else {
//                    mWordCategoryAdapter.swapCursor(cursor);
                    binding.noResultsTextView.setVisibility(View.GONE);
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
            showAddEditWordCategoryDialog(null);
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

        WordCategory wordCategory = mWordCategoryAdapter.getCurrentList().get(info.getPosition());
        switch (item.getItemId()) {
            case R.id.menu_rename_category:
                showAddEditWordCategoryDialog(wordCategory);
                return true;
            case R.id.menu_delete_category:
                showDeleteWordCategoryDialog(wordCategory);
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

    private void showAddEditWordCategoryDialog(WordCategory wordCategory) {
        WordCategoryUiModel uiModel = null;
        if (wordCategory != null) {
            uiModel = WordCategoryUiModelKt.toUiModel(wordCategory);
        }
        AddEditWordCategoryDialog.newInstance(uiModel)
                .show(getParentFragmentManager(), null);
    }

    private void showDeleteWordCategoryDialog(WordCategory wordCategory) {
        DeleteWordCategoryDialog.newInstance(WordCategoryUiModelKt.toUiModel(wordCategory))
                .show(getParentFragmentManager(), null);
    }
}
