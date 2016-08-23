package com.akhbulatov.wordkeeper.database;

import android.provider.BaseColumns;

/**
 * Creates a contract that specifies the structure of tables in the database
 */
public final class DatabaseContract {

    static final String SQL_CREATE_WORD_ENTRIES =
            "CREATE TABLE " + WordEntry.TABLE_NAME + " ("
                    + WordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WordEntry.COLUMN_NAME + " TEXT, "
                    + WordEntry.COLUMN_TRANSLATION + " TEXT);";

    static final String SQL_WORD_ADD_COLUMN_DATETIME = "ALTER TABLE " + WordEntry.TABLE_NAME
            + " ADD COLUMN " + WordEntry.COLUMN_DATETIME + " INTEGER;";

    static final String SQL_WORD_ORDER_BY_NAME = WordEntry.COLUMN_NAME + " ASC";
    static final String SQL_WORD_ORDER_BY_DATETIME = WordEntry.COLUMN_DATETIME + " DESC";

    // Prevents the initialization of an instance of the class contract
    private DatabaseContract() {
    }

    public static abstract class WordEntry implements BaseColumns {

        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TRANSLATION = "translation";
        public static final String COLUMN_DATETIME = "datetime";
    }
}
