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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akhbulatov.wordkeeper.R;

/**
 * Helps to support and update databases
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wordkeeper.db";
    private static final int DATABASE_VERSION = 3;

    private Context mContext;

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 1) {
            db.execSQL(DatabaseContract.SQL_CREATE_WORD_ENTRIES);
        }
        if (oldVersion < 2) {
            db.execSQL(DatabaseContract.SQL_WORD_ADD_COLUMN_DATETIME);
        }
        if (oldVersion < 3) {
            db.execSQL(DatabaseContract.SQL_WORD_ADD_COLUMN_CATEGORY);
            db.execSQL(DatabaseContract.SQL_CREATE_CATEGORY_ENTRIES);

            // Creates a default category that cannot be deleted
            db.insert(DatabaseContract.CategoryEntry.TABLE_NAME, null, createDefaultCategory());
        }
    }

    private ContentValues createDefaultCategory() {
        String defaultCategory = mContext.getResources().getString(R.string.default_category);

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, defaultCategory);
        return values;
    }
}
