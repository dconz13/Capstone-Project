package com.conz13.d.strongpasswordcreator;

import android.test.AndroidTestCase;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;
import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.HashSet;

/**
 * Created by dillon on 4/27/16.
 */
public class DatabaseTest extends AndroidTestCase{


    private final String SQL_CREATE_PASSWORD_TABLE = "CREATE TABLE " +
            PasswordContract.PasswordEntry.TABLE_NAME + " (" +
            PasswordContract.PasswordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PasswordContract.PasswordEntry.HEADER_TITLE + " TEXT UNIQUE NOT NULL, " +
            PasswordContract.PasswordEntry.USERNAME + " TEXT, " +
            PasswordContract.PasswordEntry.PASSWORD + " TEXT, " +
            PasswordContract.PasswordEntry.ADD_INFO + " TEXT " + ");";

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

        deleteDatabase();

        PasswordDbHelper dbHelper = new PasswordDbHelper(mContext);

        /**
         * NOTES:
         * You must first call open or create with the password and file before you can call the
         * dbHelper class. Otherwise it crashes. The openOrCreateDatabase call creates the database
         * with the password.
         */
        File databaseFile = mContext.getDatabasePath(PasswordDbHelper.DATABASE_NAME);
        databaseFile.mkdirs();
        databaseFile.delete();
        SQLiteDatabase.openOrCreateDatabase(databaseFile,PASSWORD,null);
        SQLiteDatabase db = dbHelper.getReadableDatabase(PASSWORD);

        assertEquals(true, db.isOpen());
    }

}
