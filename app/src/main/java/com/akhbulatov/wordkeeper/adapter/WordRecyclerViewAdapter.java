package com.akhbulatov.wordkeeper.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;

/**
 * Provides display list items in RecyclerView
 * and replaces the text in the view when displaying the item on the screen
 */
public class WordRecyclerViewAdapter
        extends CursorRecyclerViewAdapter<WordRecyclerViewAdapter.WordViewHolder> {

    public WordRecyclerViewAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View wordView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);

        return new WordViewHolder(wordView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, Cursor cursor) {
        if (cursor.moveToPosition(cursor.getPosition())) {
            viewHolder.mTextWordName.
                    setText(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME)));
            viewHolder.mTextWordTranslation.
                    setText(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION)));
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextWordName;
        private TextView mTextWordTranslation;

        public WordViewHolder(View itemView) {
            super(itemView);
            mTextWordName = (TextView) itemView.findViewById(R.id.text_word_name);
            mTextWordTranslation = (TextView) itemView.findViewById(R.id.text_word_translation);

            itemView.setLongClickable(true);
        }
    }
}
