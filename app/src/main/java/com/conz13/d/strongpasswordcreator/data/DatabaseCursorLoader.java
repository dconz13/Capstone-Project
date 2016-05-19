package com.conz13.d.strongpasswordcreator.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

import net.sqlcipher.database.SQLiteDatabase;


/**
 * Created by dillon on 5/19/16.
 *
 * Copied from the CursorLoader source code but instead of using the ContentResolver it performs
 * raw database calls. This is necessary to use with sqlcipher so that I can pass the password.
 */
public class DatabaseCursorLoader extends CursorLoader{
    private final String mPassword;
    final ForceLoadContentObserver mObserver;

    String[] mColumns;
    String mSelection;
    String [] mSelectionArgs;
    String mGroupBy;
    String mHaving;
    String mOrderBy;
    String mLimit;
    CancellationSignal mCancellationSignal;


    public DatabaseCursorLoader(Context context, String password){
        super(context);
        mPassword = password;
        mObserver = new ForceLoadContentObserver();
    }

    public DatabaseCursorLoader(Context context, String password, String[] columns,
                                String selection, String[] selectionArgs, String groupBy, String having,
                                String orderBy, String limit){
        super(context);
        mObserver = new ForceLoadContentObserver();
        mPassword = password;
        mColumns = columns;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
        mLimit = limit;

    }

    /**
     * Runs on a worker thread
     * Copied and edited from the source code for CursorLoader. This is where the ContentResolver is
     * replaced with raw database calls.
     * @return Cursor
     */
    @Override
    public Cursor loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }
        SQLiteDatabase db = new PasswordDbHelper(getContext()).getWritableDatabase(mPassword);;
        try {
            Cursor cursor = db.query(
                    PasswordContract.PasswordEntry.TABLE_NAME,
                    mColumns,
                    mSelection,
                    mSelectionArgs,
                    mGroupBy,
                    mHaving,
                    mOrderBy,
                    mLimit
            );

            if (cursor != null) {
                try {
                    // Ensure the cursor window is filled.
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                } catch (RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }
            return cursor;
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
                if(db.isOpen()) db.close();
            }
        }
    }
}