package com.conz13.d.strongpasswordcreator;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.helper.ItemTouchHelperViewHolder;


/**
 * Created by dillon on 5/10/16.
 */
public class LockerRecyclerAdapter extends RecyclerView.Adapter<LockerRecyclerAdapter.LockerViewHolder> {

    private Cursor mCursor;
    private OnEditButtonPressed mOnEditButtonPressed;

    public class LockerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            ItemTouchHelperViewHolder {
        public final TextView mHeaderTextView;
        public final ImageButton mEditButton;
        public final TextView mUsernameTextView;
        public final TextView mPasswordTextView;
        public final TextView mAddInfoTextView;
        public long ID;
        public final CardView mCardViewBody;

        public LockerViewHolder(View view){
            super(view);
            mHeaderTextView = (TextView)view.findViewById(R.id.locker_item_title);
            mEditButton = (ImageButton)view.findViewById(R.id.locker_edit_button);
            mUsernameTextView = (TextView)view.findViewById(R.id.locker_item_username);
            mPasswordTextView = (TextView)view.findViewById(R.id.locker_item_password);
            mAddInfoTextView = (TextView)view.findViewById(R.id.locker_item_additional_info);
            ID = -1;
            mCardViewBody = (CardView)view.findViewById(R.id.locker_card);

            mCardViewBody.setOnClickListener(this);
            mEditButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.locker_edit_button) {
                // Execute edit button
                mOnEditButtonPressed.startEditActivity(ID);
                v.getRootView().findViewById(R.id.child_layout).setVisibility(View.GONE);
            } else {
                if(v.findViewById(R.id.child_layout).getVisibility() == View.GONE) {
                    v.findViewById(R.id.child_layout).setVisibility(View.VISIBLE);
                } else {
                    v.findViewById(R.id.child_layout).setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onItemSelected() {
            // Shows that this is the selected view
            itemView.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onItemClear() {
            // Reset background color and reset text alpha to opaque
            itemView.setBackgroundColor(0);
            itemView.setAlpha(1.0f);
        }
    }

    public LockerRecyclerAdapter(OnEditButtonPressed editButtonPressed){
        mOnEditButtonPressed = editButtonPressed;
    }

    @Override
    public LockerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locker_list_item_parent, parent, false);
        return new LockerViewHolder(view);
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(LockerViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.mHeaderTextView.setText(mCursor.getString(LockerFragment.COL_HEADER_TITLE));


        String username = mCursor.getString(LockerFragment.COL_USERNAME);
        holder.mUsernameTextView.setText(username);

        String password = mCursor.getString(LockerFragment.COL_PASSWORD);
        holder.mPasswordTextView.setText(password);

        String add_info = mCursor.getString(LockerFragment.COL_ADD_INFO);
        holder.mAddInfoTextView.setText(add_info);

        holder.ID = mCursor.getLong(0);
    }

    @Override
    public int getItemCount() {
        if(null == mCursor)
            return 0;
        return mCursor.getCount();
    }

}
