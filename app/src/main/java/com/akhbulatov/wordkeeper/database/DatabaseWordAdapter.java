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

package com.akhbulatov.wordkeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Provides functionality to work with the table "Words"
 */
public class DatabaseWordAdapter extends DatabaseAdapter {

    public DatabaseWordAdapter(Context context) {
        super(context);
    }

    public long addRecord(String name, String translation, String category) {
        ContentValues values = createContentValues(name, translation, category);
        return mDatabase.insert(WordEntry.TABLE_NAME, null, values);
    }

    public boolean updateRecord(long rowId, String name, String translation, String category) {
        ContentValues values = createContentValues(name, translation, category);
        return mDatabase.update(WordEntry.TABLE_NAME, values, WordEntry._ID + "=" + rowId, null) > 0;
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

        Cursor cursor = mDatabase.query(WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION},
                null, null, null, null,
                orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchRecord(long rowId) {
        Cursor cursor = mDatabase.query(true, WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION,
                        WordEntry.COLUMN_CATEGORY},
                WordEntry._ID + "=" + rowId,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchRecordsByName(String name) {
        Cursor cursor = mDatabase.query(WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION},
                WordEntry.COLUMN_NAME + "=? COLLATE NOCASE",
                new String[]{name},
                null, null,
                DatabaseContract.SQL_WORD_ORDER_BY_NAME);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchRecordsByCategory(String category) {
        Cursor cursor = mDatabase.query(WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION,
                        WordEntry.COLUMN_CATEGORY},
                WordEntry.COLUMN_CATEGORY + "=?",
                new String[]{category},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private ContentValues createContentValues(String name, String translation, String category) {
        ContentValues values = new ContentValues();
        values.put(WordEntry.COLUMN_NAME, name);
        values.put(WordEntry.COLUMN_TRANSLATION, translation);
        values.put(WordEntry.COLUMN_DATETIME, System.currentTimeMillis());
        values.put(WordEntry.COLUMN_CATEGORY, category);
        return values;
    }
}
