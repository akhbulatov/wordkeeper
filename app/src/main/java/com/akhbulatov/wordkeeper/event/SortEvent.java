package com.akhbulatov.wordkeeper.event;

/**
 * Created by alidi on 11.07.2017.
 */

public class SortEvent {

    private int mMode;

    public SortEvent(int mode) {
        mMode = mode;
    }

    public int getMode() {
        return mMode;
    }
}
