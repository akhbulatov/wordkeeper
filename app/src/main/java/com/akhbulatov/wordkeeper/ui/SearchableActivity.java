package com.akhbulatov.wordkeeper.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.adapter.WordRecyclerViewAdapter;
import com.akhbulatov.wordkeeper.database.DatabaseAdapter;
import com.akhbulatov.wordkeeper.ui.widget.ContextMenuRecyclerView;
import com.akhbulatov.wordkeeper.ui.widget.DividerItemDecoration;

/**
 * Displays the search results words
 */
public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        // Receives a query from ACTION_SEARCH
        String query = intent.getStringExtra(SearchManager.QUERY);

        DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        ContextMenuRecyclerView wordListRecycler =
                (ContextMenuRecyclerView) findViewById(R.id.recycler_word_list);
        wordListRecycler.setHasFixedSize(true);
        wordListRecycler.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        wordListRecycler.setLayoutManager(new LinearLayoutManager(this));

        WordRecyclerViewAdapter wordRecyclerAdapter =
                new WordRecyclerViewAdapter(dbAdapter.fetchRecordsByName(query));
        wordRecyclerAdapter.setHasStableIds(true);
        wordListRecycler.setAdapter(wordRecyclerAdapter);

        TextView textNoSearchResults = (TextView) findViewById(R.id.text_no_search_results);

        if (wordRecyclerAdapter.getItemCount() == 0) {
            String noSearchResults = String.format(getString(R.string.msg_no_search_results), query);

            textNoSearchResults.setText(noSearchResults);
            textNoSearchResults.setVisibility(View.VISIBLE);
        } else {
            textNoSearchResults.setVisibility(View.INVISIBLE);
        }

        // Closes the database already here.
        // After the search results display access to the database is no longer needed
        dbAdapter.close();
    }
}
