package com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.ItemWordCategoryBinding
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordCategoryAdapter.WordCategoryViewHolder
import com.akhbulatov.wordkeeper.presentation.ui.global.list.viewholders.BaseViewHolder

class WordCategoryAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<WordCategory, WordCategoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordCategoryViewHolder {
        val binding = ItemWordCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WordCategoryViewHolder(
        private val binding: ItemWordCategoryBinding
    ) : BaseViewHolder<WordCategory>(binding.root) {

        private lateinit var wordCategory: WordCategory

        init {
            itemView.setOnClickListener { itemClickListener.onItemClick(wordCategory) }
        }

        override fun bind(item: WordCategory) {
            wordCategory = item
            with(binding) {
                val ctx = itemView.context
                nameTextView.text = item.name
                numOfWordsTextView.text = ctx.resources.getQuantityString(
                    R.plurals.item_word_category_num_of_words, item.words.size, item.words.size
                )

                if (bindingAdapterPosition == 0) {
                    moreOptionsTextView.isVisible = false
                    moreOptionsTextView.setOnClickListener(null)
                    itemView.isLongClickable = false
                } else {
                    moreOptionsTextView.isVisible = true
                    moreOptionsTextView.setOnClickListener { itemClickListener.onMoreOptionsClick(itemView) }
                    itemView.isLongClickable = true
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(wordCategory: WordCategory)
        fun onMoreOptionsClick(view: View)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordCategory>() {
            override fun areItemsTheSame(oldItem: WordCategory, newItem: WordCategory): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: WordCategory, newItem: WordCategory): Boolean =
                oldItem == newItem
        }
    }
}