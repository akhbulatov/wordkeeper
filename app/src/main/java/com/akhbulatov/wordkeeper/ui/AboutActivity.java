package com.akhbulatov.wordkeeper.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.akhbulatov.wordkeeper.BuildConfig;
import com.akhbulatov.wordkeeper.R;

/**
 * Displays an activity with information about the app
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String appVersion = String.format(getString(R.string.app_version),
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE);

        TextView textAppVersion = (TextView) findViewById(R.id.text_app_version);
        if (textAppVersion != null) {
            textAppVersion.setText(appVersion);
        }
    }
}
