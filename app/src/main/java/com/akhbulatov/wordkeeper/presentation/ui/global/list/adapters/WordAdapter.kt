package com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.contains
import androidx.core.util.forEach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.ItemWordBinding
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.presentation.ui.global.list.adapters.WordAdapter.WordViewHolder
import com.akhbulatov.wordkeeper.presentation.ui.global.list.viewholders.BaseViewHolder
import com.akhbulatov.wordkeeper.presentation.ui.global.utils.color

class WordAdapter(
    private val onItemClickListener: OnItemClickListener? = null
) : ListAdapter<Word, WordViewHolder>(DIFF_CALLBACK) {

    private val selectedWords = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWordBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedWordPositions(): List<Int> {
        val words = arrayListOf<Int>()
        selectedWords.forEach { key, _ ->
            words.add(key)
        }
        return words
    }

    fun getSelectedWordCount(): Int = selectedWords.size()

    fun toggleSelection(position: Int) {
        if (selectedWords[position, false]) {
            selectedWords.delete(position)
        } else {
            selectedWords.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun clearSelection() {
        val selection = getSelectedWordPositions()
        selectedWords.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    inner class WordViewHolder(private val binding: ItemWordBinding) : BaseViewHolder<Word>(binding.root) {

        init {
            itemView.setOnClickListener { onItemClickListener?.onItemClick(bindingAdapterPosition) }
            itemView.setOnLongClickListener { onItemClickListener?.onItemLongClick(bindingAdapterPosition) ?: false }
        }

        override fun bind(item: Word) {
            with(binding) {
                val ctx = itemView.context
                nameTextView.text = item.name
                translationTextView.text = item.translation

                val selectedItemColor = if (selectedWords.contains(bindingAdapterPosition)) {
                    ctx.color(R.color.selected_list_item)
                } else {
                    Color.TRANSPARENT
                }
                itemView.setBackgroundColor(selectedItemColor)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int): Boolean
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean =
                oldItem == newItem
        }
    }
}
