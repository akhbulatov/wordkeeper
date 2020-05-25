package com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.ItemWordBinding
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordAdapter.WordViewHolder
import com.akhbulatov.wordkeeper.presentation.ui.global.list.viewholders.BaseViewHolder
import com.akhbulatov.wordkeeper.presentation.ui.global.utils.color
import java.util.ArrayList

class WordAdapter(
    private val itemClickListener: OnItemClickListener? = null
) : ListAdapter<Word, WordViewHolder>(DIFF_CALLBACK) {

    private val mSelectedWords: SparseBooleanArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    val selectedWords: List<Int>
        get() {
            val words: MutableList<Int> = ArrayList(mSelectedWords.size())
            for (i in 0 until mSelectedWords.size()) {
                words.add(mSelectedWords.keyAt(i))
            }
            return words
        }

    val selectedWordCount: Int get() = mSelectedWords.size()

    fun toggleSelection(position: Int) {
        if (mSelectedWords[position, false]) {
            mSelectedWords.delete(position)
        } else {
            mSelectedWords.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun clearSelection() {
        val selection = selectedWords
        mSelectedWords.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    private fun isSelected(position: Int): Boolean = selectedWords.contains(position)

    inner class WordViewHolder(
        private val binding: ItemWordBinding
    ) : BaseViewHolder<Word>(binding.root) {

        private lateinit var word: Word

        init {
            itemView.setOnClickListener { itemClickListener?.onItemClick(word, bindingAdapterPosition) }
            itemView.setOnLongClickListener { itemClickListener?.onItemLongClick(word, bindingAdapterPosition) ?: false }
        }

        override fun bind(item: Word) {
            word = item
            with(binding) {
                nameTextView.text = item.name
                translationTextView.text = item.translation

                val selectedItemColor = if (isSelected(bindingAdapterPosition)) {
                    itemView.context.color(R.color.selected_list_item)
                } else {
                    Color.TRANSPARENT
                }
                itemView.setBackgroundColor(selectedItemColor)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(word: Word, position: Int)
        fun onItemLongClick(word: Word, position: Int): Boolean
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem == newItem
        }
    }
}