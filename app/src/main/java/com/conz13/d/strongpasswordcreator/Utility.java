package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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
     * 'diceware.properties' file located in the assets folder.
     *
     * @param context context for the AssetManager
     * @param number number used to look up diceware passphrase
     */

    static public String getPropertyValue(Context context, String number) {

        InputStream input = null;
        String temp = "";
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            input = assetManager.open("diceware.properties");
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
     * Generates a random 5 digit number where each digit is between 1 and 6
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

    public static String convertIntArrayToString(int[] input){
        String temp = "";
        for (int i : input){
            temp = temp.concat(Integer.toString(i));
        }
        return temp;
    }

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

}