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

package com.akhbulatov.wordkeeper.model;

import android.database.Cursor;

import com.akhbulatov.wordkeeper.database.DatabaseContract.CategoryEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alidibir Akhbulatov
 * @since 19.11.2016
 */
public class Category {

    private long mId;
    private String mName;

    public Category() {
    }

    public Category(long id) {
        mId = id;
    }

    public Category(String name) {
        mName = name;
    }

    public Category(long id, String name) {
        mId = id;
        mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public static List<Category> getCategories(Cursor cursor) {
        List<Category> categories = new ArrayList<>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            Category category = new Category();
            category.setId(cursor.getLong(cursor.getColumnIndex(CategoryEntry._ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME)));
            categories.add(category);
            cursor.moveToNext();
        }
        return categories;
    }
}
