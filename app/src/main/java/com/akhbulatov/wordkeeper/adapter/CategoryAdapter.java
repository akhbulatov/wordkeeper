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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;
import com.akhbulatov.wordkeeper.database.WordDatabaseAdapter;
import com.akhbulatov.wordkeeper.ui.activity.CategoryContentActivity;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Provides display a list of categories in RecyclerView
 */
public class CategoryAdapter extends CursorRecyclerViewAdapter<CategoryAdapter.CategoryViewHolder> {

    public static final String EXTRA_CATEGORY_NAME = "com.akhbulatov.wordkeeper.EXTRA_CATEGORY_NAME";

    private WordDatabaseAdapter mWordDbAdapter;

    public CategoryAdapter(Cursor cursor, WordDatabaseAdapter wordDbAdapter) {
        super(cursor);
        mWordDbAdapter = wordDbAdapter;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder viewHolder, Cursor cursor) {
        Context context = viewHolder.itemView.getContext();

        String categoryName = cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        viewHolder.setCategoryName(categoryName);
        viewHolder.setNumberOfWords(getNumberOfWords(context, cursor));

        // Makes the default category of non-editable
        String defaultCategory = context.getResources().getString(R.string.default_category);
        if (defaultCategory.equals(categoryName)) {
            viewHolder.setMoreOptionsVisibility(View.GONE);
            viewHolder.itemView.setLongClickable(false);
        } else {
            viewHolder.setMoreOptionsVisibility(View.VISIBLE);
            viewHolder.itemView.setLongClickable(true);
        }
    }

    private String getNumberOfWords(Context context, Cursor cursor) {
        String categoryName = cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        Cursor cursorRecords = mWordDbAdapter.getRecordsByCategory(categoryName);

        int count = cursorRecords.getCount();
        return context.getResources().getQuantityString(R.plurals.number_of_words, count, count);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextCategoryName;
        private TextView mTextNumberOfWords;
        private ImageView mImageMoreOptions;

        CategoryViewHolder(final View itemView) {
            super(itemView);
            mTextCategoryName = itemView.findViewById(R.id.text_category_name);
            mTextNumberOfWords = itemView.findViewById(R.id.text_number_of_words);
            mImageMoreOptions = itemView.findViewById(R.id.image_more_options);

            itemView.setLongClickable(true);
            itemView.setOnClickListener(v -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, CategoryContentActivity.class);
                intent.putExtra(EXTRA_CATEGORY_NAME, mTextCategoryName.getText().toString());
                context.startActivity(intent);
            });
            mImageMoreOptions.setOnClickListener(v -> itemView.showContextMenu());
        }

        void setCategoryName(String name) {
            mTextCategoryName.setText(name);
        }

        void setNumberOfWords(String number) {
            mTextNumberOfWords.setText(number);
        }

        void setMoreOptionsVisibility(int visibility) {
            mImageMoreOptions.setVisibility(visibility);
        }
    }
}