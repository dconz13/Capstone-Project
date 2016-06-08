package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dillon on 4/21/16.
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initChangePass();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Context context = getActivity().getApplicationContext();
        String key = preference.getKey();

        if(key.equals(context.getString(R.string.change_password_key))){
            ((SettingsActivity) getActivity()).showChangePasswordDialog();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void initChangePass(){
        PreferenceManager prefs = getPreferenceManager();
        Context context = getActivity().getApplicationContext();

        if(((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN()){
            prefs.findPreference(context.getString(R.string.change_password_key)).setEnabled(false);
        }
    }
}
