package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.os.AsyncTask;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

/**
 * Created by dillon on 6/17/16.
 *
 * This AsyncTask tries to open a readable database with the provided password. It will only
 * return true if the password is correct. Otherwise SQLCipher will throw an exception.
 */
public class VerifyPasswordTask extends AsyncTask<String, Void, Boolean> {
    private Context mContext;

    public VerifyPasswordTask(Context context){
        mContext = context;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        PasswordDbHelper dbHelper = new PasswordDbHelper(mContext);
        String password = params[0];

        try {
            dbHelper.getReadableDatabase(password);
            return true;
        } catch (Exception e){
            //Log.e(LOG_TAG, e.getMessage());
            return false;
        } finally{
            dbHelper.close();
        }
    }
}
