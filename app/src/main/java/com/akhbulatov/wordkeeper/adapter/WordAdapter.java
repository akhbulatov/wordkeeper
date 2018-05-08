/*
 * Copyright 2018 Alidibir Akhbulatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhbulatov.wordkeeper.adapter;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides display a list of words in RecyclerView.
 * Saves the state of the selection for ActionMode
 */
public class WordAdapter extends CursorRecyclerViewAdapter<WordAdapter.WordViewHolder> {

    private SparseBooleanArray mSelectedWords;
    private WordItemClickListener mListener;

    public WordAdapter(Cursor cursor) {
        super(cursor);
        mSelectedWords = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, Cursor cursor) {
        viewHolder.bind(cursor, isSelected(cursor.getPosition()), mListener);
    }

    public List<Integer> getSelectedWords() {
        List<Integer> words = new ArrayList<>(mSelectedWords.size());
        for (int i = 0; i < mSelectedWords.size(); i++) {
            words.add(mSelectedWords.keyAt(i));
        }
        return words;
    }

    public void setOnItemClickListener(WordItemClickListener listener) {
        mListener = listener;
    }

    public int getSelectedWordCount() {
        return mSelectedWords.size();
    }

    public void toggleSelection(int position) {
        if (mSelectedWords.get(position, false)) {
            mSelectedWords.delete(position);
        } else {
            mSelectedWords.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelection() {
        List<Integer> selection = getSelectedWords();
        mSelectedWords.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    private boolean isSelected(int position) {
        return getSelectedWords().contains(position);
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextWordName;
        private TextView mTextWordTranslation;

        WordViewHolder(View itemView) {
            super(itemView);
            mTextWordName = itemView.findViewById(R.id.text_word_name);
            mTextWordTranslation = itemView.findViewById(R.id.text_word_translation);
        }

        void bind(Cursor cursor, boolean selected, WordItemClickListener listener) {
            mTextWordName.setText((cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME))));
            mTextWordTranslation.setText((cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION))));

            int selectedItemColor = ContextCompat.getColor(itemView.getContext(), R.color.selected_list_item);
            itemView.setBackgroundColor(selected ? selectedItemColor : Color.TRANSPARENT);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onWordItemClick(getAdapterPosition());
            });
            itemView.setOnLongClickListener(v ->
                    listener != null && listener.onWordItemLongClick(getAdapterPosition()));
        }
    }

    public interface WordItemClickListener {
        void onWordItemClick(int position);

        boolean onWordItemLongClick(int position);
    }
}
