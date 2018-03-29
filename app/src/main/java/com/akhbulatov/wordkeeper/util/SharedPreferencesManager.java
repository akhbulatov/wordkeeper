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

package com.akhbulatov.wordkeeper.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alidi on 13.07.2017.
 */

public class SharedPreferencesManager {
    private static final String PREF_FILE_NAME = "wordkeeper.prefs";
    private static final String PREF_SORT_MODE = "PREF_SORT_MODE";

    public static int getSortMode(Context context) {
        return getSharedPreferencesFile(context).getInt(PREF_SORT_MODE, 1);  // 1 is sort mode by default
    }

    public static void setSortMode(Context context, int sortMode) {
        getSharedPreferencesFile(context).edit().putInt(PREF_SORT_MODE, sortMode).apply();
    }

    private static SharedPreferences getSharedPreferencesFile(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}
