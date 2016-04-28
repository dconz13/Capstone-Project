package com.conz13.d.strongpasswordcreator;

import android.test.AndroidTestCase;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;
import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.HashSet;

/**
 * Created by dillon on 4/27/16.
 */
public class DatabaseTest extends AndroidTestCase{

    private static final String PASSWORD = "test123";

    /**
     * To ensure that the database is always empty.
     */

    public void setUp(){
        deleteDatabase();
        SQLiteDatabase.loadLibs(mContext);
    }

    public void deleteDatabase(){
        mContext.deleteDatabase(PasswordDbHelper.DATABASE_NAME);
    }

    public void testCreateDatabase(){
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(PasswordContract.PasswordEntry.TABLE_NAME);

        //SQLiteDatabase.loadLibs(mContext);
        deleteDatabase();
        SQLiteDatabase db = new PasswordDbHelper(mContext).getWritableDatabase(PASSWORD);
        assertEquals(true, db.isOpen());

    }
}
