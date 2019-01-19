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

package com.akhbulatov.wordkeeper.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.util.SharedPreferencesManager;

/**
 * @author Alidibir Akhbulatov
 * @since 18.08.2016
 */

/**
 * Shows a dialog to select the mode of sorting the words in the list of words
 */
public class WordSortDialog extends BaseDialogFragment {

    private WordSortDialogListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (WordSortDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement "
                    + WordSortDialogListener.class.getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        int sortMode = SharedPreferencesManager.getSortMode(getActivity());

        return builder.setTitle(R.string.action_sort_word)
                .setSingleChoiceItems(R.array.sort_words, sortMode,
                        (dialog, which) -> {
                            mListener.onFinishWordSortDialog(which);
                            SharedPreferencesManager.setSortMode(getActivity(), which);
                            dialog.dismiss();
                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> dialog.dismiss())
                .create();
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
