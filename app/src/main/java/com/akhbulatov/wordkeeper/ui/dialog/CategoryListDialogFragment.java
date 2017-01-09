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
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.database.CategoryDatabaseAdapter;
import com.akhbulatov.wordkeeper.model.Category;

import java.util.List;

/**
 * @author Alidibir Akhbulatov
 * @since 04.11.2016
 */
public class CategoryListDialogFragment extends DialogFragment {

    private String[] mCategories;

    private CategoryListDialogListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (CategoryListDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement "
                    + CategoryListDialogListener.class.getName());
        }

        // Gets category list from the database
        CategoryDatabaseAdapter categoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
        categoryDbAdapter.open();

        Cursor cursor = categoryDbAdapter.getAll();
        List<Category> categoryList = Category.getCategories(cursor);
        mCategories = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            mCategories[i] = categoryList.get(i).getName();
        }

        cursor.close();
        categoryDbAdapter.close();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle(R.string.category_list_title)
                .setItems(mCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFinishCategoryListDialog(mCategories[which]);
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

    public interface CategoryListDialogListener {
        void onFinishCategoryListDialog(String category);
    }
}
