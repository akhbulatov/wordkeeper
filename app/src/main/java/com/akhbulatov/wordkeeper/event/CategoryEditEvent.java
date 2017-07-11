package com.akhbulatov.wordkeeper.event;

import android.support.v4.app.DialogFragment;

/**
 * Created by alidi on 11.07.2017.
 */

public class CategoryEditEvent extends EditEvent {

    public CategoryEditEvent(DialogFragment dialog, int positiveTextId) {
        super(dialog, positiveTextId);
    }
}
