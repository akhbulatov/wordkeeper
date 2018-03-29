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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.event.SortEvent;
import com.akhbulatov.wordkeeper.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Alidibir Akhbulatov
 * @since 18.08.2016
 */

/**
 * Shows a dialog to select the mode of sorting the words in the list of words
 */
public class WordSortDialog extends BaseDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int sortMode = SharedPreferencesManager.getSortMode(getActivity());

        return builder.setTitle(R.string.action_sort_word)
                .setSingleChoiceItems(R.array.sort_words, sortMode,
                        (dialog, which) -> {
                            EventBus.getDefault().post(new SortEvent(which));
                            SharedPreferencesManager.setSortMode(getActivity(), which);
                            dialog.dismiss();
                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> dialog.dismiss())
                .create();
    }
}
