package com.conz13.d.strongpasswordcreator;


import android.view.View;

/**
 * Created by dillon on 5/11/16.
 *
 * Used to launch a new activity from the locker fragment
 */
public interface OnEditButtonPressed {
    void startEditActivity(long Id, View view);
}
