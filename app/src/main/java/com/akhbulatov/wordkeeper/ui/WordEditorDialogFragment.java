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

package com.akhbulatov.wordkeeper.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.akhbulatov.wordkeeper.R;

/**
 * Displays editor dialog box to add words and edit words
 */
public class WordEditorDialogFragment extends DialogFragment {

    /**
     * The keys are used as arguments.
     * Those keys get values sent to dialog box
     */
    private static final String ARGUMENT_TITLE_ID = "title";
    private static final String ARGUMENT_POSITIVE_TEXT_ID = "positiveText";
    private static final String ARGUMENT_NEGATIVE_TEXT_ID = "negativeText";

    /**
     * Stores the value obtained in the arguments.
     * These values are used to set the title and text on the buttons
     */
    private int mTitleId;
    private int mPositiveTextId;
    private int mNegativeTextId;

    private WordEditorDialogListener mListener;

    public static WordEditorDialogFragment newInstance(int titleId, int positiveTextId,
                                                       int negativeTextId) {
        WordEditorDialogFragment dialog = new WordEditorDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TITLE_ID, titleId);
        args.putInt(ARGUMENT_POSITIVE_TEXT_ID, positiveTextId);
        args.putInt(ARGUMENT_NEGATIVE_TEXT_ID, negativeTextId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WordEditorDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement WordEditorDialogListener");
        }
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        return builder.setView(inflater.inflate(R.layout.dialog_word_editor, null))
                .setTitle(mTitleId)
                .setPositiveButton(mPositiveTextId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPositiveClick(WordEditorDialogFragment.this, mPositiveTextId);
                    }
                })
                .setNegativeButton(mNegativeTextId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    /**
     * Delivers information about the event activity or fragment that opened the dialog
     */
    public interface WordEditorDialogListener {
        /**
         * @param dialog         The dialog box where the event occurred
         * @param positiveTextId ID of the text on the positive button
         */
        void onPositiveClick(DialogFragment dialog, int positiveTextId);
    }
}
