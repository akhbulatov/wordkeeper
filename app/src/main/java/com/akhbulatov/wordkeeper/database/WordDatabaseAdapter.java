/*
 * Copyright 2019 Alidibir Akhbulatov
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

    public WordDatabaseAdapter(Context context) {
        super(context);
    }

    @Override
    public int update(Word word) {
        ContentValues values = createContentValues(word);
        return mDatabase.update(WordEntry.TABLE_NAME,
                values,
                WordEntry._ID + " = ?",
                new String[]{String.valueOf(word.getId())});
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
