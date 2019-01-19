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
import android.util.Log;

import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;
import com.akhbulatov.wordkeeper.database.dao.CategoryDao;
import com.akhbulatov.wordkeeper.model.Category;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Provides functionality to work with the table "Categories"
 */
public class CategoryDatabaseAdapter extends DatabaseAdapter implements CategoryDao {

    private static final String TAG = CategoryDatabaseAdapter.class.getSimpleName();

    public CategoryDatabaseAdapter(Context context) {
        super(context);
    }

    @Override
    public long insert(Category category) {
        ContentValues values = createContentValues(category);
        return mDatabase.insert(CategoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public int update(Category category) {
        ContentValues values = createContentValues(category);
        return mDatabase.update(CategoryEntry.TABLE_NAME,
                values,
                CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    @Override
    public int delete(Category category) {
        return mDatabase.delete(CategoryEntry.TABLE_NAME,
                CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    @Override
    public Cursor getAll() {
        Cursor cursor = mDatabase.query(CategoryEntry.TABLE_NAME,
                new String[]{CategoryEntry._ID, CategoryEntry.COLUMN_NAME},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public Category get(long id) {
        Category category = null;
        Cursor cursor = mDatabase.query(true, CategoryEntry.TABLE_NAME,
                new String[]{CategoryEntry._ID, CategoryEntry.COLUMN_NAME},
                CategoryEntry._ID + " = " + id,
                null, null, null, null, null);

        try {
            cursor.moveToFirst();
            category = new Category();
            category.setId(cursor.getLong(cursor.getColumnIndex(CategoryEntry._ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME)));
        } catch (Exception e) {
            Log.e(TAG, "Could not get the record");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return category;
    }

    private ContentValues createContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        return values;
    }
}
