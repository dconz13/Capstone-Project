package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by dillon on 4/21/16.
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        //Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.settings_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }
}
