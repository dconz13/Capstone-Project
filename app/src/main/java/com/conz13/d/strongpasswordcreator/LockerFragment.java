package com.conz13.d.strongpasswordcreator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

/**
 * Created by dillon on 4/17/16.
 */
public class LockerFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,
            OnEditButtonPressed {
    private static final String LOG_TAG = LockerFragment.class.getSimpleName();

    private static final int LOADER = 0;

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

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LockerRecyclerAdapter mAdapter;

    // TODO: Add hiding the toolbar on scroll of the list
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.locker_layout, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.locker_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LockerRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        ((MainActivity)getActivity()).updateNavItemSelected(MainActivity.LOCKER);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO: add sort options in locker menu
        String sortOrder = PasswordContract.PasswordEntry.HEADER_TITLE + " ASC";

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
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void startEditActivity(long Id) {
        // Send bundle of arguments from the view
        Intent data = new Intent(getContext(), EditActivity.class);
        data.putExtras(Utility.buildBundleFromId(getContext(), Id));
        startActivity(data);
    }
}
