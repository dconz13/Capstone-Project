package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dillon on 4/17/16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }
}
