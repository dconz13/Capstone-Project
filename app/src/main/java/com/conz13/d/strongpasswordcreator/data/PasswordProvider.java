package com.conz13.d.strongpasswordcreator.data;

import android.annotation.TargetApi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.conz13.d.strongpasswordcreator.MainActivity;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryBuilder;

/**
 * Created by dillon on 4/17/16.
 */
public class PasswordProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private PasswordDbHelper mDbHelper;

    static final int DATA = 100;

    private static final SQLiteQueryBuilder mPasswordQueryBuilder;
    private String PASSWORD;

    // If the setTables isn't called for the SQLiteQueryBuilder object
    // when working with SQLCipher, Invalid Tables error will be thrown.
    static{
        mPasswordQueryBuilder = new SQLiteQueryBuilder();
        mPasswordQueryBuilder.setTables(PasswordContract.PasswordEntry.TABLE_NAME);
        // TODO: Use Utility method to access KeyChain to get the password after it is saved
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PasswordContract.CONTENT_AUTHORITY;

        // Add more Uri's if more paths are created for the contract
        uriMatcher.addURI(authority, PasswordContract.PATH_DATA, DATA);
        return uriMatcher;
    }

    private Cursor getDataCursor(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return mPasswordQueryBuilder.query(mDbHelper.getReadableDatabase(PASSWORD),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    // Set password here from ContentResolver.call() method before doing any ContentProvider actions
    @Nullable
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if(method.equals("setPASSWORD") && null != arg){
            setPASSWORD(arg);
        }
        return super.call(method, arg, extras);
    }

    protected void setPASSWORD(String password){
        this.PASSWORD = password;
    }

    @Override
    public boolean onCreate() {
        SQLiteDatabase.loadLibs(getContext());
        mDbHelper = new PasswordDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull  Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch(mUriMatcher.match(uri)){
            // "data"
            case DATA:{
                cursor = getDataCursor(projection, selection, selectionArgs, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri  uri) {
        // Seems silly to switch 2 possible outcomes but this is the framework for the future
        switch(mUriMatcher.match(uri)){
            case DATA:
                return PasswordContract.PasswordEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase(PASSWORD);
        Uri tempUri;

        switch(mUriMatcher.match(uri)){
            case DATA:{
                long _id = db.insert(PasswordContract.PasswordEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    tempUri = PasswordContract.PasswordEntry.buildPasswordUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return tempUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase(PASSWORD);
        int rowsDeleted;

        //if(null == selection )
        //selection = "1";
        switch(mUriMatcher.match(uri)){
            case DATA: {
                rowsDeleted = db.delete(PasswordContract.PasswordEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }

        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase(PASSWORD);
        int rowsUpdated;

        switch(mUriMatcher.match(uri)){
            case DATA:{
                rowsUpdated = db.update(PasswordContract.PasswordEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }

        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    // Properly close the database during testing
    @Override
    @TargetApi(16)
    public void shutdown(){
        mDbHelper.close();
        super.shutdown();
    }
}