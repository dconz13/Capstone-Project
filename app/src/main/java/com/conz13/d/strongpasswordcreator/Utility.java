package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dillon on 4/17/16.
 */
public class Utility {

    protected Utility(){
    }

    private static final String LOG_TAG = Utility.class.getSimpleName();

    static public String getPropertyValue(Context context, int number) {

        InputStream input = null;
        String temp = "";
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            input = assetManager.open("diceware.properties");
            properties.load(input);

            temp = properties.getProperty(Integer.toString(number));

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

}