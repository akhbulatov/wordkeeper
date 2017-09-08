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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.akhbulatov.wordkeeper.App;
import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.event.CategoryEditEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Alidibir Akhbulatov
 * @since 18.09.2016
 */

/**
 * Shows an editor dialog to add and edit categories
 */
public class CategoryEditorDialog extends DialogFragment {

    private static final String ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID";
    private static final String ARGUMENT_POSITIVE_TEXT_ID = "ARGUMENT_POSITIVE_TEXT_ID";
    private static final String ARGUMENT_NEGATIVE_TEXT_ID = "ARGUMENT_NEGATIVE_TEXT_ID";

    private int mTitleId;
    private int mPositiveTextId;
    private int mNegativeTextId;

    public static CategoryEditorDialog newInstance(int titleId,
                                                   int positiveTextId,
                                                   int negativeTextId) {
        CategoryEditorDialog dialog = new CategoryEditorDialog();

        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TITLE_ID, titleId);
        args.putInt(ARGUMENT_POSITIVE_TEXT_ID, positiveTextId);
        args.putInt(ARGUMENT_NEGATIVE_TEXT_ID, negativeTextId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTitleId = args.getInt(ARGUMENT_TITLE_ID);
            mPositiveTextId = args.getInt(ARGUMENT_POSITIVE_TEXT_ID);
            mNegativeTextId = args.getInt(ARGUMENT_NEGATIVE_TEXT_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_category_editor, null))
                .setTitle(mTitleId)
                .setPositiveButton(mPositiveTextId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new CategoryEditEvent(CategoryEditorDialog.this,
                                mPositiveTextId));
                    }
                })
                .setNegativeButton(mNegativeTextId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        // Shows the soft keyboard automatically
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getRefWatcher(getActivity()).watch(this);
    }
}
