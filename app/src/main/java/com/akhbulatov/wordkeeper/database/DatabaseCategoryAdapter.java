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

import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Provides functionality to work with the table "Categories"
 */
public class DatabaseCategoryAdapter extends DatabaseAdapter {

    public DatabaseCategoryAdapter(Context context) {
        super(context);
    }

    public long addRecord(String name) {
        ContentValues values = createContentValues(name);
        return mDatabase.insert(CategoryEntry.TABLE_NAME, null, values);
    }

    public boolean updateRecord(long rowId, String name) {
        ContentValues values = createContentValues(name);
        return mDatabase.update(CategoryEntry.TABLE_NAME, values,
                CategoryEntry._ID + "=" + rowId, null) > 0;
    }

    public boolean deleteRecord(long rowId) {
        return mDatabase.delete(CategoryEntry.TABLE_NAME, CategoryEntry._ID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllRecords() {
        Cursor cursor = mDatabase.query(CategoryEntry.TABLE_NAME,
                new String[]{CategoryEntry._ID, CategoryEntry.COLUMN_NAME},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchRecord(long rowId) {
        Cursor cursor = mDatabase.query(true, CategoryEntry.TABLE_NAME,
                new String[]{CategoryEntry._ID, CategoryEntry.COLUMN_NAME},
                CategoryEntry._ID + "=" + rowId,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private ContentValues createContentValues(String name) {
        ContentValues values = new ContentValues();
        values.put(CategoryEntry.COLUMN_NAME, name);
        return values;
    }
}
