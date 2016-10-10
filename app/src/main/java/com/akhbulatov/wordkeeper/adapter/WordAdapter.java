/*
 * Copyright 2016 Alidibir Akhbulatov <alidibir.akhbulatov@gmail.com>
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

    private WordViewHolder.WordAdapterListener mListener;

    public WordAdapter(Cursor cursor) {
        super(cursor);
        mSelectedWords = new SparseBooleanArray();

        mListener = null;
    }

    public WordAdapter(Cursor cursor, WordViewHolder.WordAdapterListener listener) {
        super(cursor);
        mSelectedWords = new SparseBooleanArray();

        mListener = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, Cursor cursor) {
        viewHolder.textWordName.
                setText(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME)));
        viewHolder.textWordTranslation.
                setText(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION)));

        viewHolder.itemView.setBackgroundColor(isSelected(cursor
                .getPosition()) ? Color.LTGRAY : Color.TRANSPARENT);
    }

    public List<Integer> getSelectedWords() {
        List<Integer> words = new ArrayList<>(mSelectedWords.size());
        for (int i = 0; i < mSelectedWords.size(); i++) {
            words.add(mSelectedWords.keyAt(i));
        }
        return words;
    }

    public int getSelectedWordCount() {
        return mSelectedWords.size();
    }

    public boolean isSelected(int position) {
        return getSelectedWords().contains(position);
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

    public static class WordViewHolder extends RecyclerView.ViewHolder {

        public TextView textWordName;
        public TextView textWordTranslation;

        private WordAdapterListener mListener;

        public WordViewHolder(View itemView, WordAdapterListener listener) {
            super(itemView);
            textWordName = (TextView) itemView.findViewById(R.id.text_word_name);
            textWordTranslation = (TextView) itemView.findViewById(R.id.text_word_translation);

            mListener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onWordItemClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mListener != null && mListener.onWordItemLongClick(getAdapterPosition());
                }
            });
        }

        public interface WordAdapterListener {
            void onWordItemClick(int position);

            boolean onWordItemLongClick(int position);
        }
    }
}
