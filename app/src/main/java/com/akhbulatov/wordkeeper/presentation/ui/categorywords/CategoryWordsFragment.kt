package com.akhbulatov.wordkeeper.presentation.ui.categorywords

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.FragmentCategoryWordsBinding
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelFactory
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordAdapter
import javax.inject.Inject

class CategoryWordsFragment : BaseFragment(R.layout.fragment_category_words) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<CategoryWordsViewModel> { viewModelFactory }

    private var _binding: FragmentCategoryWordsBinding? = null
    private val binding get() = _binding!!

    private val wordAdapter by lazy { WordAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .categoryWordsComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        val wordCategory = requireArguments().getString(ARG_WORD_CATEGORY)!!
        if (savedInstanceState == null) {
            viewModel.loadWordsByCategory(wordCategory)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryWordsBinding.bind(view)
        with(binding) {
            wordsRecyclerView.setHasFixedSize(true)
            wordsRecyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            wordsRecyclerView.adapter = wordAdapter
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            renderViewState(viewState)
        })
    }

    override fun onDestroyView() {
        binding.wordsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun renderViewState(viewState: CategoryWordsViewModel.ViewState) {
        showEmptyProgress(viewState.emptyProgress)
        showEmptyData(viewState.emptyData)
        showEmptyError(viewState.emptyError.first, viewState.emptyError.second)
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

    private fun showWords(show: Boolean, words: List<Word>) {
        wordAdapter.submitList(words)
        binding.wordsRecyclerView.isVisible = show
    }

    companion object {
        private const val ARG_WORD_CATEGORY = "word_category"

        fun newInstance(wordCategory: String) = CategoryWordsFragment().apply {
            arguments = bundleOf(ARG_WORD_CATEGORY to wordCategory)
        }
    }
}
