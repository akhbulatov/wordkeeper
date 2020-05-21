/*
 * Copyright 2019 Alidibir Akhbulatov
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
import android.database.Cursor;
import android.os.Bundle;

import com.akhbulatov.wordkeeper.R;
import com.akhbulatov.wordkeeper.database.CategoryDatabaseAdapter;
import com.akhbulatov.wordkeeper.model.Category;
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * @author Alidibir Akhbulatov
 * @since 04.11.2016
 */
public class CategoryListDialog extends BaseDialogFragment {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        return builder.setTitle(R.string.category_list_title)
                .setItems(mCategories, (dialog, which) -> {
                    mListener.onFinishCategoryListDialog(mCategories[which]);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();
    }

    public interface CategoryListDialogListener {
        void onFinishCategoryListDialog(String category);
    }
}
