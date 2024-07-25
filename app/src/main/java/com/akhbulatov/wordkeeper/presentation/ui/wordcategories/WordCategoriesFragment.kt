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
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.FragmentWordCategoriesBinding
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.presentation.ui.addeditword.AddEditWordDialog
import com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory.AddEditWordCategoryDialog
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.base.ConfirmDialog
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
        WordCategoriesComponent.create().inject(this)
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
            wordCategoriesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        addWordFab.hide()
                    } else {
                        addWordFab.show()
                    }
                }
            })
            wordCategoriesRecyclerView.adapter = wordCategoryAdapter
            registerForContextMenu(wordCategoriesRecyclerView)

            addWordFab.setOnClickListener { showAddWordDialog() }
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { renderViewState(it) })
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
                viewModel.onSearchWordCategoryChanged(newText)
                return true
            }
        })
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.onCloseSearchWordCategoryClicked()
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
        showEmptySearchResult(viewState.emptySearchResult.first, viewState.emptySearchResult.second)
        showWordCategories(viewState.wordCategories.first, viewState.wordCategories.second)
    }

    private fun showEmptyProgress(show: Boolean) {
        binding.emptyProgressBar.isVisible = show
    }

    private fun showEmptyData(show: Boolean) {
        binding.emptyDataTextView.isVisible = show
    }

    private fun showEmptyError(show: Boolean, message: String?) {
        binding.errorTextView.text = message
        binding.errorTextView.isVisible = show
    }

    private fun showEmptySearchResult(show: Boolean, query: String?) {
        query?.let {
            val emptyResult = String.format(getString(R.string.word_categories_empty_search_result), it.htmlEncode())
            binding.emptySearchResultTextView.text = emptyResult.parseAsHtml()
        }
        binding.emptySearchResultTextView.isVisible = show
    }

    private fun showWordCategories(show: Boolean, wordCategories: List<WordCategory>) {
        wordCategoryAdapter.submitList(wordCategories)
        binding.wordCategoriesRecyclerView.isVisible = show
    }

    private fun showAddEditWordCategoryDialog(wordCategory: WordCategory?) {
        setFragmentResultListener(AddEditWordCategoryDialog.REQUEST_WORD_CATEGORY) { _, bundle ->
            val category = bundle.getString(AddEditWordCategoryDialog.RESULT_WORD_CATEGORY)!!
            if (wordCategory == null) {
                viewModel.onAddWordCategoryClicked(category)
            } else {
                viewModel.onEditWordCategoryClicked(wordCategory.id, category)
            }
        }
        AddEditWordCategoryDialog.newInstance(wordCategory?.toUiModel())
            .show(parentFragmentManager, null)
    }

    private fun showDeleteWordCategoryDialog(wordCategory: WordCategory) {
        setFragmentResultListener(ConfirmDialog.REQUEST_OK) { _, _ ->
            viewModel.onDeleteWordCategoryWithWordsClicked(wordCategory)
        }
        ConfirmDialog.newInstance(
            R.string.delete_word_category_title,
            R.string.delete_word_category_message,
            R.string.msg_action_delete
        )
            .show(parentFragmentManager, null)
    }

    private fun showAddWordDialog() {
        AddEditWordDialog.newInstance()
            .show(parentFragmentManager, null)
    }
}
