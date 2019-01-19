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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.akhbulatov.wordkeeper.R;

/**
 * Shows an editor dialog to add and edit words
 */
public class WordEditorDialog extends BaseDialogFragment {

    private static final String ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID";
    private static final String ARGUMENT_POSITIVE_TEXT_ID = "ARGUMENT_POSITIVE_TEXT_ID";
    private static final String ARGUMENT_NEGATIVE_TEXT_ID = "ARGUMENT_NEGATIVE_TEXT_ID";

    private int mTitleId;
    private int mPositiveTextId;
    private int mNegativeTextId;

    private WordEditorDialogListener mListener;

    public static WordEditorDialog newInstance(@StringRes int titleId,
                                               @StringRes int positiveTextId,
                                               @StringRes int negativeTextId) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TITLE_ID, titleId);
        args.putInt(ARGUMENT_POSITIVE_TEXT_ID, positiveTextId);
        args.putInt(ARGUMENT_NEGATIVE_TEXT_ID, negativeTextId);

        WordEditorDialog dialog = new WordEditorDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WordEditorDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + WordEditorDialogListener.class.getName());
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

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_word_editor, null))
                .setTitle(mTitleId)
                .setPositiveButton(mPositiveTextId, (dialog, which) ->
                        mListener.onFinishWordEditorDialog(WordEditorDialog.this, mPositiveTextId))
                .setNegativeButton(mNegativeTextId, (dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.create();
        // Shows the soft keyboard automatically
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface WordEditorDialogListener {
        /**
         * Applies the changes to the edited word in the dialog
         *
         * @param dialog         The current open dialog
         * @param positiveTextId The ID of the text on the positive button
         */
        void onFinishWordEditorDialog(DialogFragment dialog, int positiveTextId);
    }
}
