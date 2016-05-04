package com.conz13.d.strongpasswordcreator;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;
import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * Created by dillon on 4/27/16.
 */
public class ProviderTest extends AndroidTestCase {

    private static final String PASSWORD = "test123";

    public void deleteRecords(){
        mContext.getContentResolver().delete(PasswordContract.PasswordEntry.CONTENT_URI,
                null,
                null);
        // Not sure if I need to use sqlcipher cursor here
        Cursor testCursor = mContext.getContentResolver().query(
                PasswordContract.PasswordEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Password table during delete", 0, testCursor.getCount());
        testCursor.close();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        SQLiteDatabase.loadLibs(mContext);
        // This causes errors with the content provider. It's only necessary if you don't have a provider.
//        File databaseFile = getContext().getDatabasePath(PasswordDbHelper.DATABASE_NAME);
//        databaseFile.mkdirs();
//        databaseFile.delete();
//        SQLiteDatabase.openOrCreateDatabase(databaseFile,PASSWORD,null);
        deleteRecords();
    }

    public void testInsert(){
        Uri testUri = PasswordContract.PasswordEntry.CONTENT_URI;
        ContentValues testValues = new ContentValues();
        // Keys must match column names
        testValues.put("header_title", "test_header");
        testValues.put("username", "test_user");
        testValues.put("password", "test_password");
        testValues.put("add_info", "test_add_info");

        // Fresh start
        deleteRecords();

        mContext.getContentResolver().insert(testUri, testValues);

        Cursor testCursor = mContext.getContentResolver().query(
                PasswordContract.PasswordEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        testCursor.moveToFirst();
        String returnString = testCursor.getString(testCursor.getColumnIndex("header_title"));
        assertEquals("test_header", returnString);
        returnString = testCursor.getString(testCursor.getColumnIndex("username"));
        assertEquals("test_user", returnString);
        returnString = testCursor.getString(testCursor.getColumnIndex("password"));
        assertEquals("test_password", returnString);
        returnString = testCursor.getString(testCursor.getColumnIndex("add_info"));
        assertEquals("test_add_info", returnString);

        testCursor.close();
    }

    // I would have reused a lot of this code but I was unsure on the way that AndroidTestCase handles
    // methods that don't have "test" as the prefix
    public void testUpdate(){
        Uri testUri = PasswordContract.PasswordEntry.CONTENT_URI;
        ContentValues testValues = new ContentValues();
        // Keys must match column names
        testValues.put("header_title", "test_header");
        testValues.put("username", "test_user");
        testValues.put("password", "test_password");
        testValues.put("add_info", "test_add_info");

        // Fresh start
        deleteRecords();

        mContext.getContentResolver().insert(testUri, testValues);
        Cursor testCursor = mContext.getContentResolver().query(
                PasswordContract.PasswordEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        testCursor.moveToFirst();
        String firstString = testCursor.getString(testCursor.getColumnIndex("header_title"));
        assertEquals("test_header", firstString);

        testValues.clear();
        testValues.put("header_title", "updated_header");
        testValues.put("username", "updated_user");
        testValues.put("password", "updated_password");
        testValues.put("add_info", "updated_add_info");
        mContext.getContentResolver().update(testUri,testValues,null,null);
        testCursor = mContext.getContentResolver().query(
                PasswordContract.PasswordEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        testCursor.moveToFirst();
        String secondString = testCursor.getString(testCursor.getColumnIndex("header_title"));
        assertEquals("updated_header", secondString);
        secondString = testCursor.getString(testCursor.getColumnIndex("username"));
        assertEquals("updated_user", secondString);
        secondString = testCursor.getString(testCursor.getColumnIndex("password"));
        assertEquals("updated_password", secondString);
        secondString = testCursor.getString(testCursor.getColumnIndex("add_info"));
        assertEquals("updated_add_info", secondString);


        testCursor.close();
    }
}
