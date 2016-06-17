package com.conz13.d.strongpasswordcreator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dillon on 4/17/16.
 */
public class LockerFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,
            OnEditButtonPressed {
    private static final String LOG_TAG = LockerFragment.class.getSimpleName();

    private static final int LOADER = 0;
    private boolean mSkipFlag;

    private static final String[] PASSWORD_COLUMNS = {
            PasswordContract.PasswordEntry.TABLE_NAME + "." + PasswordContract.PasswordEntry._ID,
            PasswordContract.PasswordEntry.HEADER_TITLE,
            PasswordContract.PasswordEntry.USERNAME,
            PasswordContract.PasswordEntry.PASSWORD,
            PasswordContract.PasswordEntry.ADD_INFO
    };

    static final int COL_PASSWORD_ID = 0;
    static final int COL_HEADER_TITLE = 1;
    static final int COL_USERNAME = 2;
    static final int COL_PASSWORD = 3;
    static final int COL_ADD_INFO = 4;

    @BindView(R.id.locker_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.locker_empty) TextView mEmptyTextView;
    @BindView(R.id.locker_empty_image) ImageView mEmptyImageView;

    private RecyclerView.LayoutManager mLayoutManager;
    private LockerRecyclerAdapter mAdapter;



    // TODO: Add parallax scrolling
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.locker_layout, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LockerRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mSkipFlag = ((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN();
        if(mSkipFlag){
            mEmptyTextView.setText(getContext().getString(R.string.locker_empty_skipped));
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyImageView.setImageDrawable(getResources().getDrawable(R.drawable.locker_locked_splash));
            mEmptyImageView.setVisibility(View.VISIBLE);
            mEmptyImageView.setContentDescription(getContext().getString(R.string.locker_empty_skipped));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.locker_menu, menu);
        if(((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN()){
            menu.findItem(R.id.locker_sign_out).setTitle(getContext().getString(R.string.sign_in));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.locker_action_settings: {
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            }
            case R.id.locker_sign_out: {
                ((MainActivity)getActivity()).signOut();
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mSkipFlag = ((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN();
        if(!mSkipFlag) {
            getLoaderManager().initLoader(LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO: add sort options in locker menu
        String sortOrder = PasswordContract.PasswordEntry.HEADER_TITLE + " ASC";
        String password = ((MyApplication)getActivity().getApplication()).getPASSWORD();
        String method = getContext().getString(R.string.database_password_method);
        getContext().getContentResolver().call(PasswordContract.BASE_CONTENT_URI, method, password, null);
        return new CursorLoader(getActivity(),
                PasswordContract.PasswordEntry.CONTENT_URI,
                PASSWORD_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() == 0) {
            mEmptyTextView.setText(getContext().getString(R.string.locker_empty_text_view));
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyImageView.setImageDrawable(getResources().getDrawable(R.drawable.locker_empty_splash));
            mEmptyImageView.setVisibility(View.VISIBLE);
            mEmptyImageView.setContentDescription(getContext().getString(R.string.locker_empty_text_view));
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void startEditActivity(long Id, View view) {
        // Send bundle of arguments from the view
        Intent data = new Intent(getContext(), EditActivity.class);
        String password = ((MyApplication)getActivity().getApplication()).getPASSWORD();
        data.putExtras(Utility.buildBundleFromId(getContext(), password, Id));
        startActivityForResult(data, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(null != data)
                if (resultCode == MainActivity.RESULT_OK) {
                    getLoaderManager().restartLoader(LOADER, null, this);
                    CharSequence message = data.getCharSequenceExtra(getString(R.string.edit_snackbar_key));
                    Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
                }
        }
    }
}
