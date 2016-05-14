package com.conz13.d.strongpasswordcreator;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * Created by dillon on 4/17/16.
 */
public class Utility {

    protected Utility(){
    }

    private static final String LOG_TAG = Utility.class.getSimpleName();

    /**
     * Gets the diceware passphrase associated with the supplied number from the
     * 'diceware_en.properties' file located in the assets folder.
     *
     * @param context context for the AssetManager
     * @param number number used to look up diceware passphrase
     */

    static public String getPropertyValue(Context context, String number) {

        InputStream input = null;
        String temp = "";
        Properties properties = new Properties();
        String language = "_en";

        try {
            AssetManager assetManager = context.getAssets();
            input = assetManager.open("diceware" + language + ".properties");
            properties.load(input);

            temp = properties.getProperty(number);

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

        return temp;
    }

    /**
     * Generates a random 5 digit number where each digit is between 1 and 6. Each digit of the number
     * is stored as it's own index in an integer array.
     *
     * @return temp (the integer array holding the 5 digit number)
     */
    public static int[] getDiceRoll(){
        Random rndNumber = new Random();
        int MAX_INT = 6;
        int temp[] = new int[5];
        for(int i = 0; i < temp.length; i++){
            temp[i] = rndNumber.nextInt(MAX_INT) + 1;
        }
        return temp;
    }

    /**
     * Converts an integer array into a single String
     *
     * @param input integer array to be converted into a String
     * @return temp (the concatenated String)
     */

    public static String convertIntArrayToString(int[] input){
        String temp = "";
        for (int i : input){
            temp = temp.concat(Integer.toString(i));
        }
        return temp;
    }

    /**
     * Gets the drawable representation for a number
     *
     * @param number generated number 1-6
     * @return returns the drawable associated with the number
     */

    public static int getDiceImage(int number){
        switch(number){
            case 1:
                return R.drawable.one;
            case 2:
                return R.drawable.two;
            case 3:
                return R.drawable.three;
            case 4:
                return R.drawable.four;
            case 5:
                return R.drawable.five;
            case 6:
                return R.drawable.six;
            default:
                return -1;
        }
    }

    /**
     * Checks the database to see if the Header name is already in use
     *
     * @param context application context
     * @param headerName header string to check the database for
     * @return returns true if the entry exists
     */
    public static boolean checkIfEntryExists(Context context, String headerName){
        Uri uri = PasswordContract.PasswordEntry.CONTENT_URI;
        String[] projection = {"header_title"};
        String selection = "header_title='" + headerName + "' ";
        boolean existsFlag = false;
        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                selection,
                null,
                null
        );
        try {

            if(cursor.moveToFirst()){
                existsFlag = true;
            }
        }catch (NullPointerException e){
            Log.e(LOG_TAG, e.getMessage());
            existsFlag = false;
        }finally{
            cursor.close();
        }
        return existsFlag;
    }

    /**
     * Updates the currently selected entry
     *
     * @param context application context
     * @param contentValues content values to update
     * @param ID used to make sure the correct entry is updated
     * @return returns true on successful update
     */
    public static boolean updateExistingEntry(Context context, ContentValues contentValues, long ID){
        Uri uri = PasswordContract.PasswordEntry.CONTENT_URI;
        String selection = "_ID='" + ID + "'";

        int rows = context.getContentResolver().update(
                uri,
                contentValues,
                selection,
                null
                );

        if(rows > 0) return true;

        else return false;
    }

    /**
     * Builds a bundle from a give id. The Id is supplied from the recycler view entry in the locker
     * @param context application context
     * @param id id of the entry the cursor will use to select the rows
     * @return returns a bundle of all values from all rows
     */
    public static Bundle buildBundleFromId(Context context, long id){
        Bundle bundle = new Bundle();
        Uri uri = PasswordContract.PasswordEntry.CONTENT_URI;
        String selection = "_ID='" + id + "'";
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                selection,
                null,
                null
        );
        try {
            if(cursor.moveToFirst()) {
                bundle.putLong(context.getString(R.string.intent_extra_id),
                        cursor.getLong(0));
                bundle.putString(context.getString(R.string.intent_extra_header),
                        cursor.getString(cursor.getColumnIndex(PasswordContract.PasswordEntry.HEADER_TITLE)));
                bundle.putString(context.getString(R.string.intent_extra_user),
                        cursor.getString(cursor.getColumnIndex(PasswordContract.PasswordEntry.USERNAME)));
                bundle.putString(context.getString(R.string.intent_extra_pass),
                        cursor.getString(cursor.getColumnIndex(PasswordContract.PasswordEntry.PASSWORD)));
                bundle.putString(context.getString(R.string.intent_extra_add_info),
                        cursor.getString(cursor.getColumnIndex(PasswordContract.PasswordEntry.ADD_INFO)));
            }
        } catch (NullPointerException e){
            Log.e(LOG_TAG, e.getMessage());
        }finally {
            cursor.close();
        }

        return bundle;
    }

}