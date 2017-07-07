/*
 * Copyright 2017 Alidibir Akhbulatov
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

package com.akhbulatov.wordkeeper.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;

/**
 * @author Alidibir Akhbulatov
 * @since 18.08.2016
 */

/**
 * Shows a dialog to select the mode of sorting the words in the list of words
 */
public class WordSortDialog extends DialogFragment {

    public static final String PREF_NAME = "wordkeeper.prefs";
    public static final String PREF_SORT_MODE = "PREF_SORT_MODE";
    public static final int DEFAULT_SORT_MODE = 1;

    private int mSortMode;

    private SharedPreferences mPrefs;

    private WordSortDialogListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (WordSortDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement "
                    + WordSortDialogListener.class.getName());
        }

        mPrefs = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mSortMode = mPrefs.getInt(PREF_SORT_MODE, DEFAULT_SORT_MODE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle(R.string.action_sort_word)
                .setSingleChoiceItems(R.array.sort_words, mSortMode,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSortMode = which;
                                mListener.onFinishWordSortDialog(mSortMode);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.dialog_action_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(PREF_SORT_MODE, mSortMode);
        editor.apply();
    }

    public interface WordSortDialogListener {
        /**
         * Uses to update the list of words when selecting the sort mode
         *
         * @param sortMode The sort mode for the list of words
         */
        void onFinishWordSortDialog(int sortMode);
    }
}
