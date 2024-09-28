package com.akhbulatov.wordkeeper.presentation.ui.words

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.ui.base.BaseFragment
import com.akhbulatov.wordkeeper.core.ui.list.adapters.WordAdapter
import com.akhbulatov.wordkeeper.core.ui.models.toUiModel
import com.akhbulatov.wordkeeper.core.ui.utils.requireCompatActivity
import com.akhbulatov.wordkeeper.core.ui.utils.showSnackbar
import com.akhbulatov.wordkeeper.databinding.FragmentWordsBinding
import com.akhbulatov.wordkeeper.domain.word.models.Word
import com.akhbulatov.wordkeeper.presentation.ui.addeditword.AddEditWordDialog
import com.akhbulatov.wordkeeper.presentation.ui.main.MainActivity
import com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory.SelectWordCategoryDialog
import com.akhbulatov.wordkeeper.presentation.ui.sortwords.SortWordsDialog
import javax.inject.Inject

class WordsFragment : BaseFragment(R.layout.fragment_words) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<WordsViewModel> { viewModelFactory }

    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!

    private val wordActionModeCallback by lazy { WordActionModeCallback() }
    private var wordActionMode: ActionMode? = null

    private val wordAdapter by lazy {
        WordAdapter(object : WordAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (wordActionMode != null) {
                    toggleSelection(position)
                }
            }

            override fun onItemLongClick(position: Int): Boolean {
                if (wordActionMode == null) {
                    wordActionMode = requireCompatActivity().startSupportActionMode(wordActionModeCallback)
                }
                toggleSelection(position)
                return true
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WordsComponent.create().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            viewModel.loadWords()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordsBinding.bind(view)
        with(binding) {
            requireCompatActivity().supportActionBar?.setTitle(R.string.words_title)

            wordsRecyclerView.setHasFixedSize(true)
            wordsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            wordsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (wordActionMode == null) {
                        if (dy > 0) {
                            addWordFab.hide()
                        } else {
                            addWordFab.show()
                        }
                    }
                }
            })
            wordsRecyclerView.adapter = wordAdapter

            addWordFab.setOnClickListener { showAddEditWordDialog(null) }
        }

        viewModel.viewState.observe(viewLifecycleOwner, { renderViewState(it) })
    }

    override fun onDestroyView() {
        binding.wordsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.words, menu)
        val searchItem = menu.findItem(R.id.menu_search_words)
        val searchView = searchItem.actionView as SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(ComponentName(requireActivity(), MainActivity::class.java))
        )
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onSearchWordChanged(newText)
                return true
            }
        })
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.onCloseSearchWordClicked()
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sort_words) {
            showSortWordsDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderViewState(viewState: WordsViewModel.ViewState) {
        showEmptyProgress(viewState.emptyProgress)
        showEmptyData(viewState.emptyData)
        showEmptyError(viewState.emptyError.first, viewState.emptyError.second)
        showEmptySearchResult(viewState.emptySearchResult.first, viewState.emptySearchResult.second)
        showWords(viewState.words.first, viewState.words.second)
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
            val emptyResult = String.format(getString(R.string.words_empty_search_result), it.htmlEncode())
            binding.emptySearchResultTextView.text = emptyResult.parseAsHtml()
        }
        binding.emptySearchResultTextView.isVisible = show
    }

    private fun showWords(show: Boolean, words: List<Word>) {
        wordAdapter.submitList(words)
        binding.wordsRecyclerView.isVisible = show
    }

    private fun toggleSelection(position: Int) {
        wordAdapter.toggleSelection(position)
        val count = wordAdapter.getSelectedWordCount()
        if (count == 0) {
            wordActionMode?.finish()
        } else {
            wordActionMode?.title = count.toString()
            wordActionMode?.invalidate()
        }
    }

    private fun showAddEditWordDialog(word: Word?) {
        AddEditWordDialog.newInstance(word?.toUiModel())
            .show(parentFragmentManager, null)
    }

    private fun showSortWordsDialog() {
        setFragmentResultListener(SortWordsDialog.REQUEST_SORT_MODE) { _, bundle ->
            val sortMode = bundle.getSerializable(SortWordsDialog.RESULT_SORT_MODE) as Word.SortMode
            viewModel.onSortWordSelected(sortMode)
        }
        SortWordsDialog.newInstance(viewModel.getWordSortMode())
            .show(requireActivity().supportFragmentManager, null)
    }

    private fun showSelectWordCategoriesDialog() {
        setFragmentResultListener(SelectWordCategoryDialog.REQUEST_SELECT_WORD_CATEGORY) { _, bundle ->
            val category = bundle.getString(SelectWordCategoryDialog.RESULT_SELECT_WORD_CATEGORY)!!
            val words = wordAdapter.getSelectedWordPositions()
                .map { wordAdapter.currentList[it] }
            viewModel.onSelectWordCatalogClicked(words.toMutableList(), category)
            wordActionMode?.finish()
            showSnackbar(R.string.words_success_move)
        }
        SelectWordCategoryDialog.newInstance(R.string.words_action_move)
            .show(requireActivity().supportFragmentManager, null)
    }

    private inner class WordActionModeCallback : ActionMode.Callback {

        private lateinit var editWordItem: MenuItem

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.selected_word, menu)
            editWordItem = menu.findItem(R.id.menu_edit_word)
            binding.addWordFab.hide()
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            editWordItem.isVisible = wordAdapter.getSelectedWordCount() == 1
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.menu_move_word -> {
                    showSelectWordCategoriesDialog()
                    true
                }
                R.id.menu_edit_word -> {
                    val firstPos = wordAdapter.getSelectedWordPositions().first()
                    val word = wordAdapter.currentList[firstPos]
                    showAddEditWordDialog(word)
                    mode.finish()
                    true
                }
                R.id.menu_delete_word -> {
                    val words = wordAdapter.getSelectedWordPositions()
                        .map { wordAdapter.currentList[it] }
                    viewModel.onDeleteWordsClicked(words)
                    mode.finish()
                    true
                }
                else -> false
            }

        override fun onDestroyActionMode(mode: ActionMode) {
            wordAdapter.clearSelection()
            wordActionMode = null
            binding.addWordFab.show()
        }
    }
}
