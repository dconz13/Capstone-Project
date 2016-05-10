package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by dillon on 5/10/16.
 */
public class LockerRecyclerAdapter extends RecyclerView.Adapter<LockerRecyclerAdapter.LockerViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public class LockerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mHeaderTextView;
        public final ImageButton mEditButton;
        public final TextView mUsernameTextView;
        public final TextView mPasswordTextView;
        public final TextView mAddInfoTextView;

        public LockerViewHolder(View view){
            super(view);
            mHeaderTextView = (TextView)view.findViewById(R.id.locker_item_title);
            mEditButton = (ImageButton)view.findViewById(R.id.locker_edit_button);
            mUsernameTextView = (TextView)view.findViewById(R.id.locker_item_username_label);
            mPasswordTextView = (TextView)view.findViewById(R.id.locker_item_password_label);
            mAddInfoTextView = (TextView)view.findViewById(R.id.locker_item_additional_info_label);

            mEditButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Execute edit button
        }
    }

    public LockerRecyclerAdapter(Context context) {
        mContext = context;
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

        String username = "Username: ";
        username = username.concat(" " + mCursor.getString(LockerFragment.COL_USERNAME));
        holder.mUsernameTextView.setText(username);

        String password = "Password: ";
        password = password.concat(" " + mCursor.getString(LockerFragment.COL_PASSWORD));
        holder.mPasswordTextView.setText(password);

        String add_info = "Additional info: ";
        add_info = add_info.concat(" " + mCursor.getString(LockerFragment.COL_ADD_INFO));
        holder.mAddInfoTextView.setText(add_info);
    }

    @Override
    public int getItemCount() {
        if(null == mCursor)
            return 0;
        return mCursor.getCount();
    }
}
