/*
 * Copyright 2017 Alidibir Akhbulatov
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

package com.akhbulatov.wordkeeper.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.CategoryAdapter;
import com.akhbulatov.wordkeeper.adapter.WordAdapter;
import com.akhbulatov.wordkeeper.database.WordDatabaseAdapter;

import butterknife.ButterKnife;

/**
 * Shows all the words from a certain category
 */
public class CategoryContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_content);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String categoryName = getIntent().getStringExtra(CategoryAdapter.EXTRA_CATEGORY_NAME);
        setTitle(categoryName);

        RecyclerView wordList = ButterKnife.findById(this, R.id.recycler_word_list);
        wordList.setHasFixedSize(true);
        wordList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        wordList.setLayoutManager(new LinearLayoutManager(this));

        WordDatabaseAdapter wordDbAdapter = new WordDatabaseAdapter(this);
        wordDbAdapter.open();

        WordAdapter wordAdapter = new WordAdapter(wordDbAdapter.getRecordsByCategory(categoryName));
        wordAdapter.setHasStableIds(true);
        wordList.setAdapter(wordAdapter);

        TextView textEmptyCategoryContent = ButterKnife.findById(this, R.id.text_empty_category_content);

        if (wordAdapter.getItemCount() == 0) {
            textEmptyCategoryContent.setVisibility(View.VISIBLE);
        } else {
            textEmptyCategoryContent.setVisibility(View.INVISIBLE);
        }

        // Closes the database already here, as it is no longer needed
        wordDbAdapter.close();
    }
}
