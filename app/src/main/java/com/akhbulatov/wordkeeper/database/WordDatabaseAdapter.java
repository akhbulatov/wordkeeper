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

package com.akhbulatov.wordkeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.akhbulatov.wordkeeper.database.DatabaseContract.WordEntry;
import com.akhbulatov.wordkeeper.database.dao.WordDao;
import com.akhbulatov.wordkeeper.model.Word;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Provides functionality to work with the table "Words"
 */
public class WordDatabaseAdapter extends DatabaseAdapter implements WordDao {

    private static final String TAG = WordDatabaseAdapter.class.getSimpleName();

    public WordDatabaseAdapter(Context context) {
        super(context);
    }

    @Override
    public long insert(Word word) {
        ContentValues values = createContentValues(word);
        return mDatabase.insert(WordEntry.TABLE_NAME, null, values);
    }

    @Override
    public int update(Word word) {
        ContentValues values = createContentValues(word);
        return mDatabase.update(WordEntry.TABLE_NAME,
                values,
                WordEntry._ID + " = ?",
                new String[]{String.valueOf(word.getId())});
    }

    @Override
    public int delete(Word word) {
        return mDatabase.delete(WordEntry.TABLE_NAME,
                WordEntry._ID + " = ?",
                new String[]{String.valueOf(word.getId())});
    }

    @Override
    public Cursor getAll(int sortMode) {
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

    @Override
    public Word get(long id) {
        Word word = null;
        Cursor cursor = mDatabase.query(true, WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION,
                        WordEntry.COLUMN_CATEGORY},
                WordEntry._ID + " = " + id,
                null, null, null, null, null);

        try {
            cursor.moveToFirst();
            word = new Word();
            word.setId(cursor.getLong(cursor.getColumnIndex(WordEntry._ID)));
            word.setName(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_NAME)));
            word.setTranslation(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_TRANSLATION)));
            word.setCategory(cursor.getString(cursor.getColumnIndex(WordEntry.COLUMN_CATEGORY)));
        } catch (Exception e) {
            Log.e(TAG, "Could not get the record");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return word;
    }

    public Cursor getRecordsByCategory(String category) {
        Cursor cursor = mDatabase.query(WordEntry.TABLE_NAME,
                new String[]{WordEntry._ID,
                        WordEntry.COLUMN_NAME,
                        WordEntry.COLUMN_TRANSLATION,
                        WordEntry.COLUMN_CATEGORY},
                WordEntry.COLUMN_CATEGORY + " = ?",
                new String[]{category},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private ContentValues createContentValues(Word word) {
        ContentValues values = new ContentValues();
        values.put(WordEntry.COLUMN_NAME, word.getName());
        values.put(WordEntry.COLUMN_TRANSLATION, word.getTranslation());
        values.put(WordEntry.COLUMN_DATETIME, System.currentTimeMillis());
        values.put(WordEntry.COLUMN_CATEGORY, word.getCategory());
        return values;
    }
}
