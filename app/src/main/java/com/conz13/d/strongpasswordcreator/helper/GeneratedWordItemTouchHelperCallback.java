package com.conz13.d.strongpasswordcreator.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.GeneratedWordRecyclerAdapter;
import com.conz13.d.strongpasswordcreator.R;

/**
 * Created by dillon on 5/11/16.
 */
public class GeneratedWordItemTouchHelperCallback extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

    private final ItemTouchHelper mItemTouchHelper;
    private Paint mPaint = new Paint();

    public GeneratedWordItemTouchHelperCallback(ItemTouchHelper helper){
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
        final int dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP | android.support.v7.widget.helper.ItemTouchHelper.DOWN;
        final int swipeFlags = android.support.v7.widget.helper.ItemTouchHelper.END;

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

    // TODO: Figure out a work around for preventing swipe towards "start" while still showing an animation
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == android.support.v7.widget.helper.ItemTouchHelper.END ){
            mItemTouchHelper.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // Make view opaque
        viewHolder.itemView.setAlpha(1.0f);

        if(viewHolder instanceof ItemTouchHelperViewHolder){
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        // Changed this from half way to 3/4 of the way
        return 0.75f;
    }

    // Credit for this method goes to Tatarize from stackoverflow
    // src: http://stackoverflow.com/questions/5896234/how-to-use-android-canvas-to-draw-a-roundrect-with-only-topleft-and-topright-cor
    static public Path preLollipopRoundedRect(float left, float top, float right, float bottom, float rx, float ry){
        Path path = new Path();
        if(rx < 0) rx = 0;
        if(ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);
        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE){
            View itemView = viewHolder.itemView;

            mPaint.setColor(itemView.getResources().getColor(R.color.red));
            Bitmap icon = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_delete_black_24dp);
            if(dX != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    c.drawRoundRect((float) itemView.getLeft(), (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), 7f, 7f, mPaint);
                } else {
                    Path path = preLollipopRoundedRect((float) itemView.getLeft() + 1f, (float) itemView.getTop() + 1f,
                            (float) itemView.getRight() - 1f, (float) itemView.getBottom() - 1f, 7f, 7f);
                    c.drawPath(path, mPaint);
                }
            }
            if(ViewCompat.getLayoutDirection(itemView) != ViewCompat.LAYOUT_DIRECTION_RTL) {
                float margin_left = (float) itemView.getLeft() + itemView.getResources().getDimension(R.dimen.default_margin);
                float margin_top = (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2;
                c.drawBitmap(icon, margin_left, margin_top, mPaint);
            }
            else {
                float margin_left = (float) itemView.getRight() - itemView.getResources().getDimension(R.dimen.default_margin) - icon.getWidth();
                float margin_top = (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2;
                c.drawBitmap(icon, margin_left, margin_top, mPaint);
            }

            TextView itemText = ((GeneratedWordRecyclerAdapter.GeneratedWordViewHolder)viewHolder).getMResultantWord();
            itemText.setAlpha(1.0f - Math.abs(dX) / (float) itemView.getWidth());
            itemView.setTranslationX(dX);

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if(actionState != android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder instanceof ItemTouchHelperViewHolder){
                ((ItemTouchHelperViewHolder)viewHolder).onItemSelected();
            }
        }
    }


}
