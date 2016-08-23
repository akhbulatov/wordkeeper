package com.akhbulatov.wordkeeper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helps with support for databases up to date
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wordkeeper.db";
    private static final int DATABASE_VERSION = 2;

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL(DatabaseContract.SQL_CREATE_WORD_ENTRIES);
        }
        if (oldVersion < 2) {
            db.execSQL(DatabaseContract.SQL_WORD_ADD_COLUMN_DATETIME);
        }
    }
}
