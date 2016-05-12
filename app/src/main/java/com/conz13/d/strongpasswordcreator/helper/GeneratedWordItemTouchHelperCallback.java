package com.conz13.d.strongpasswordcreator.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.GeneratedWordRecyclerAdapter;
import com.conz13.d.strongpasswordcreator.R;

/**
 * Created by dillon on 5/11/16.
 */
public class GeneratedWordItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final GeneratedWordItemTouchHelper mItemTouchHelper;
    private Paint mPaint = new Paint();

    public GeneratedWordItemTouchHelperCallback(GeneratedWordItemTouchHelper helper){
        mItemTouchHelper = helper;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }

        mItemTouchHelper.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    // TODO: Figure out a work around for preventing swipe to start while still showing an animation
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.END ){
            mItemTouchHelper.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // Make view opaque
        viewHolder.itemView.setAlpha(1.0f);

        if(viewHolder instanceof GeneratedWordItemTouchHelperViewHolder){
            ((GeneratedWordItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        // Changed this from half way to 3/4 of the way
        return 0.75f;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View itemView = viewHolder.itemView;

            if(dX >0){
                mPaint.setColor(itemView.getResources().getColor(R.color.red));
                Bitmap icon = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_delete_black_24dp);
                c.drawRect((float) itemView.getLeft(),(float) itemView.getTop(),
                        (float) itemView.getRight(),(float) itemView.getBottom(),mPaint);

                float margin_left = (float) itemView.getLeft() + itemView.getResources().getDimension(R.dimen.default_margin)*2;
                float margin_top = (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2;
                c.drawBitmap(icon, margin_left, margin_top, mPaint);

                TextView itemText = ((GeneratedWordRecyclerAdapter.GeneratedWordViewHolder)viewHolder).mResultantWord;
                itemText.setAlpha(1.0f - Math.abs(dX) / (float) itemView.getWidth());
                itemView.setTranslationX(dX);
            }

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder instanceof GeneratedWordItemTouchHelperViewHolder){
                ((GeneratedWordItemTouchHelperViewHolder)viewHolder).onItemSelected();
            }
        }
    }


}