package com.akhbulatov.wordkeeper.event;

/**
 * Created by alidi on 11.07.2017.
 */

public class CategoryEvent {

    private String mName;

    public CategoryEvent(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
