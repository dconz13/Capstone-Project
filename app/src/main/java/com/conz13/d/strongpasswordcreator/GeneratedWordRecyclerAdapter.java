package com.conz13.d.strongpasswordcreator;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.helper.ClearDeleteButton;
import com.conz13.d.strongpasswordcreator.helper.ItemTouchHelper;
import com.conz13.d.strongpasswordcreator.helper.ItemTouchHelperViewHolder;
import com.conz13.d.strongpasswordcreator.helper.OnDragListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dillon on 5/5/16.
 */
public class GeneratedWordRecyclerAdapter extends RecyclerView.Adapter<GeneratedWordRecyclerAdapter.GeneratedWordViewHolder>
    implements ItemTouchHelper {
    private ArrayList<String> mWords;
    private ClearDeleteButton mClearDeleteButton;
    private OnDragListener mOnDragListener;
    private Boolean isRtl;

    public class GeneratedWordViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        @BindView(R.id.reorder_button) ImageView mReorderButton;
        @BindView(R.id.resultant_word) TextView mResultantWord;

        public GeneratedWordViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onItemSelected() {
            // Shows that this is the selected view
        }
        public TextView getMResultantWord(){
            return this.mResultantWord;
        }

        @Override
        public void onItemClear() {
            // Reset background color and reset text alpha to opaque
            mResultantWord.setAlpha(1.0f);
        }

    }

    public GeneratedWordRecyclerAdapter(Boolean rtl, ArrayList<String> resultantWords,
                                        ClearDeleteButton clearDeleteButton,
                                        OnDragListener onDragListener){
        isRtl = rtl;
        mWords = resultantWords;
        mClearDeleteButton = clearDeleteButton;
        mOnDragListener = onDragListener;
    }

    @Override
    public GeneratedWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resultant_word_list_item, parent, false);

        return new GeneratedWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GeneratedWordViewHolder holder, int position) {
        if(null != mWords.get(position)) {
            holder.mResultantWord.setText(mWords.get(position));
            if(isRtl){
                holder.mResultantWord.setGravity(Gravity.END);
            }

            holder.mReorderButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN){
                        mOnDragListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mWords,fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    // TODO: Store last deleted item in a variable so it can be restored with an undo button
    @Override
    public void onItemDismiss(int position) {
        mWords.remove(position);
        notifyItemRemoved(position);
        if(mWords.size() == 0){
            mClearDeleteButton.clearDeleteButton();
        }
    }

}
