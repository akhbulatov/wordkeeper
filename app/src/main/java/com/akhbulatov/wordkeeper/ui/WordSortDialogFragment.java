package com.akhbulatov.wordkeeper.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.akhbulatov.wordkeeper.R;

/**
 * @author Alidibir Akhbulatov
 * @since 18.08.2016
 */

/**
 * Displays a dialog box to select the mode of sorting the words in the list
 */
public class WordSortDialogFragment extends DialogFragment {

    public static final String PREF_NAME = "wordkeeper_prefs";
    public static final String PREF_SORT_MODE = "sortMode";

    private int mSortMode;

    private WordSortDialogListener mListener;
    private SharedPreferences mPrefs;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WordSortDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement WordSortDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // If the sort mode is not set use default "Last modified" (value is 1)
        mSortMode = mPrefs.getInt(PREF_SORT_MODE, 1);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle(R.string.title_word_sort)
                .setSingleChoiceItems(R.array.word_sorts, mSortMode,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSortMode = which;
                                dialog.dismiss();
                                mListener.onSingleChoiceClick(mSortMode);
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

    /**
     * Uses to refresh the words list when choosing the sort mode
     */
    public interface WordSortDialogListener {
        /**
         * @param sortMode The sorting mode for the list of words
         */
        void onSingleChoiceClick(int sortMode);
    }
}
