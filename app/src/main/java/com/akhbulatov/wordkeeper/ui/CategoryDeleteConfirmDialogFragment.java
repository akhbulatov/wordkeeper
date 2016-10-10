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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;

/**
 * @author Alidibir Akhbulatov
 * @since 24.09.2016
 */

/**
 * Shows a dialog to confirm deletion of category
 */
public class CategoryDeleteConfirmDialogFragment extends DialogFragment {

    private CategoryDeleteConfirmListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (CategoryDeleteConfirmListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement "
                    + CategoryDeleteConfirmListener.class.getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(R.string.confirm_category_delete_title)
                .setMessage(R.string.confirm_category_delete_message)
                .setPositiveButton(R.string.confirm_category_action_delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onFinishCategoryDeleteConfirmDialog(
                                        CategoryDeleteConfirmDialogFragment.this);
                            }
                        })
                .setNegativeButton(R.string.confirm_category_action_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create();
    }

    public interface CategoryDeleteConfirmListener {
        /**
         * Applies the changes when the category is deleted
         *
         * @param dialog The current open dialog
         */
        void onFinishCategoryDeleteConfirmDialog(DialogFragment dialog);
    }
}