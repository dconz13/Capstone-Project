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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by dillon on 4/21/16.
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initChangePass();

        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_ad_test_unit_id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        AdView adView = (AdView) rootView.findViewById(R.id.settings_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return rootView;
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
