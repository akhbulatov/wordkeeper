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
