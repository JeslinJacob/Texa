package com.texaconnect.texa.ui.common;

import android.support.annotation.Nullable;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

public abstract class BaseExpandedListAdapter<T> extends BaseExpandableListAdapter {

    @Nullable
    private List<T> items;
    // each time data is set, we update this variable so that if DiffUtil calculation returns
    // after repetitive updates, we can ignore the old calculation
    private int dataVersion = 0;

}
