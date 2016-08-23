package com.akhbulatov.wordkeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;

/**
 * Sets the connection to the database through a helper and directly
 */
public class DatabaseAdapter {

    private SQLiteDatabase mDatabase;
    private DatabaseOpenHelper mDbHelper;

    public DatabaseAdapter(Context context) {
        mDbHelper = new DatabaseOpenHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long addRecord(String name, String translation) {
        ContentValues wordValues = createContentValues(name, translation);
        return mDatabase.insert(WordEntry.TABLE_NAME, null, wordValues);
    }

    public boolean updateRecord(long rowId, String name, String translation) {
        ContentValues updateValues = createContentValues(name, translation);
        return mDatabase.update(WordEntry.TABLE_NAME,
                updateValues, WordEntry._ID + "=" + rowId, null) > 0;
    }

    public boolean deleteRecord(long rowId) {
        return mDatabase.delete(WordEntry.TABLE_NAME, WordEntry._ID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllRecords(int sortMode) {
        String orderBy;
        // Uses only 2 sort mode
        // For sorting by name value is 0 and by last modified is value 1
        if (sortMode == 0) {
            orderBy = DatabaseContract.SQL_WORD_ORDER_BY_NAME;
        } else {
            orderBy = DatabaseContract.SQL_WORD_ORDER_BY_DATETIME;
        }

        return mDatabase.query(DatabaseContract.WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID, WordEntry.COLUMN_NAME, WordEntry.COLUMN_TRANSLATION},
                null, null, null, null,
                orderBy);
    }

    public Cursor fetchRecord(long rowId) {
        Cursor cursor = mDatabase.query(true, WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID, WordEntry.COLUMN_NAME, WordEntry.COLUMN_TRANSLATION},
                WordEntry._ID + "=" + rowId,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchRecordsByName(String name) {
        return mDatabase.query(WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID, WordEntry.COLUMN_NAME, WordEntry.COLUMN_TRANSLATION},
                WordEntry.COLUMN_NAME + "=? COLLATE NOCASE",
                new String[]{name},
                null, null,
                DatabaseContract.SQL_WORD_ORDER_BY_NAME);
    }

    private ContentValues createContentValues(String name, String translation) {
        ContentValues values = new ContentValues();
        values.put(WordEntry.COLUMN_NAME, name);
        values.put(WordEntry.COLUMN_TRANSLATION, translation);
        values.put(WordEntry.COLUMN_DATETIME, System.currentTimeMillis());
        return values;
    }
}
