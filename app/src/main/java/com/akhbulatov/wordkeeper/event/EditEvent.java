package com.akhbulatov.wordkeeper.event;

import android.support.v4.app.DialogFragment;

/**
 * Created by alidi on 12.07.2017.
 */

public abstract class EditEvent {

    private DialogFragment mDialog;
    private int mPositiveTextId;

    /**
     * Creates an instance for EventBus
     *
     * @param dialog         The current open dialog
     * @param positiveTextId The ID of the text on the positive button
     */
    EditEvent(DialogFragment dialog, int positiveTextId) {
        mDialog = dialog;
        mPositiveTextId = positiveTextId;
    }

    public DialogFragment getDialog() {
        return mDialog;
    }

    public int getPositiveTextId() {
        return mPositiveTextId;
    }
}
