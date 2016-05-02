package com.conz13.d.strongpasswordcreator.data;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import com.conz13.d.strongpasswordcreator.data.PasswordContract.PasswordEntry;


/**
 * Created by dillon on 4/17/16.
 */
public class PasswordDbHelper extends SQLiteOpenHelper {

    // Increment version if the schema is changed
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "password.db";

    public PasswordDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PASSWORD_TABLE = "CREATE TABLE " +
                PasswordEntry.TABLE_NAME + " (" +
                PasswordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PasswordEntry.HEADER_TITLE + " TEXT UNIQUE NOT NULL, " +
                PasswordEntry.USERNAME + " TEXT, " +
                PasswordEntry.PASSWORD + " TEXT, " +
                PasswordEntry.ADD_INFO + " TEXT " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PasswordEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
