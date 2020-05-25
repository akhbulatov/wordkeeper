package com.akhbulatov.wordkeeper.presentation.ui.wordsofcategory

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.FragmentWordOfCategoryBinding
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordAdapter
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import com.akhbulatov.wordkeeper.presentation.ui.global.models.toDomainModel

class WordsOfCategoryFragment : BaseFragment(R.layout.fragment_word_of_category) {

    private var _binding: FragmentWordOfCategoryBinding? = null
    private val binding get() = _binding!!

    private val wordAdapter = WordAdapter()
    private lateinit var wordCategory: WordCategoryUiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordCategory = requireArguments().getParcelable(ARG_WORD_CATEGORY)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordOfCategoryBinding.bind(view)

        wordAdapter.submitList(wordCategory.words.map { it.toDomainModel() })
        binding.wordsRecyclerView.setHasFixedSize(true)
        binding.wordsRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.wordsRecyclerView.adapter = wordAdapter
    }

    override fun onDestroyView() {
        binding.wordsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_WORD_CATEGORY = "word_category"

        @JvmStatic
        fun newInstance(wordCategory: WordCategoryUiModel) = WordsOfCategoryFragment().apply {
            arguments = bundleOf(ARG_WORD_CATEGORY to wordCategory)
        }
    }
}