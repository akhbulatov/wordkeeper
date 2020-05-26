package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.FragmentWordCategoriesBinding
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory.AddEditWordCategoryDialog
import com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory.DeleteWordCategoryDialog
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordCategoryAdapter
import com.akhbulatov.wordkeeper.presentation.ui.global.models.toUiModel
import com.akhbulatov.wordkeeper.presentation.ui.global.utils.requireCompatActivity
import com.akhbulatov.wordkeeper.presentation.ui.global.views.ContextMenuRecyclerView.RecyclerContextMenuInfo
import com.akhbulatov.wordkeeper.presentation.ui.main.MainActivity
import javax.inject.Inject

class WordCategoriesFragment : BaseFragment(R.layout.fragment_word_categories) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<WordCategoriesViewModel> { viewModelFactory }

    private var _binding: FragmentWordCategoriesBinding? = null
    private val binding get() = _binding!!

    private val wordCategoryAdapter by lazy {
        WordCategoryAdapter(object : WordCategoryAdapter.OnItemClickListener {
            override fun onItemClick(wordCategory: WordCategory) {
                viewModel.onWordCategoryClicked(wordCategory)
            }

            override fun onMoreOptionsClick(view: View) {
                view.showContextMenu()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .wordCategoriesComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            viewModel.loadWordCategories()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordCategoriesBinding.bind(view)
        with(binding) {
            requireCompatActivity().supportActionBar?.setTitle(R.string.word_categories_title)

            wordCategoriesRecyclerView.setHasFixedSize(true)
            wordCategoriesRecyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            wordCategoriesRecyclerView.adapter = wordCategoryAdapter
            registerForContextMenu(wordCategoriesRecyclerView)

            addWordFab.setOnClickListener {
                // todo
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            renderViewState(viewState)
        })
    }

    override fun onDestroyView() {
        binding.wordCategoriesRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.word_categories, menu)
        val searchItem = menu.findItem(R.id.menu_search_word_categories)
        val searchView = searchItem.actionView as SearchView
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(ComponentName(requireContext(), MainActivity::class.java))
        )
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                // todo
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_word_category) {
            showAddEditWordCategoryDialog(null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.selected_word_category, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as RecyclerContextMenuInfo
        val wordCategory = wordCategoryAdapter.currentList[info.position]
        return when (item.itemId) {
            R.id.menu_rename_word_category -> {
                showAddEditWordCategoryDialog(wordCategory)
                true
            }
            R.id.menu_delete_word_category -> {
                showDeleteWordCategoryDialog(wordCategory)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun renderViewState(viewState: WordCategoriesViewModel.ViewState) {
        showEmptyProgress(viewState.emptyProgress)
        showEmptyData(viewState.emptyData)
        showEmptyError(viewState.emptyError.first, viewState.emptyError.second)
        showWordCategories(viewState.wordCategories.first, viewState.wordCategories.second)
    }

    private fun showEmptyProgress(show: Boolean) {
//        binding.emptyProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private fun showEmptyData(show: Boolean) {
//        binding.emptyDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private fun showEmptyError(show: Boolean, message: String?) {
//        binding.errorTextView.setText(message);
//        binding.errorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private fun showWordCategories(show: Boolean, wordCategories: List<WordCategory>) {
        wordCategoryAdapter.submitList(wordCategories)
        binding.wordCategoriesRecyclerView.isVisible = show
    }

    private fun showAddEditWordCategoryDialog(wordCategory: WordCategory?) {
        AddEditWordCategoryDialog.newInstance(wordCategory?.toUiModel())
            .show(parentFragmentManager, null)
    }

    private fun showDeleteWordCategoryDialog(wordCategory: WordCategory) {
        DeleteWordCategoryDialog.newInstance(wordCategory.toUiModel())
            .show(parentFragmentManager, null)
    }
}