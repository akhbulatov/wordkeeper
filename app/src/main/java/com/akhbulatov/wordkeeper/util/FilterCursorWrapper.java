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

package com.akhbulatov.wordkeeper.util;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * @author Alidibir Akhbulatov
 * @since 24.11.2016
 */
public class FilterCursorWrapper extends CursorWrapper {

    private int[] mIndex;
    private int mCount;
    private int mPos;

    public FilterCursorWrapper(Cursor cursor, String filter, int column) {
        super(cursor);
        filter = filter.toLowerCase();
        if (filter.length() != 0) {
            mCount = super.getCount();
            mIndex = new int[mCount];
            for (int i = 0; i < mCount; i++) {
                super.moveToPosition(i);
                if (getString(column).toLowerCase().contains(filter)) {
                    mIndex[mPos++] = i;
                }
            }
            mCount = mPos;
            mPos = 0;
            super.moveToFirst();
        } else {
            mCount = super.getCount();
            mIndex = new int[mCount];
            for (int i = 0; i < mCount; i++) {
                mIndex[i] = i;
            }
        }
    }

    @Override
    public boolean move(int offset) {
        return moveToPosition(mPos + offset);
    }

    @Override
    public boolean moveToNext() {
        return moveToPosition(mPos + 1);
    }

    @Override
    public boolean moveToPrevious() {
        return moveToPosition(mPos - 1);
    }

    @Override
    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    @Override
    public boolean moveToLast() {
        return moveToPosition(mCount - 1);
    }

    @Override
    public boolean moveToPosition(int position) {
        if (position >= mCount || position < 0) {
            return false;
        }
        mPos = position;
        return super.moveToPosition(mIndex[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public int getPosition() {
        return mPos;
    }
}