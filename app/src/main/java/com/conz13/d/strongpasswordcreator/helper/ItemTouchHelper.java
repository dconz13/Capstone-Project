package com.conz13.d.strongpasswordcreator.helper;

/**
 * Created by dillon on 5/11/16.
 */
public interface ItemTouchHelper {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}