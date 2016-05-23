package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_layout, container, false);
        TextView changePass = (TextView) rootView.findViewById(R.id.change_pass_setting);
        initChangePass(changePass);

        return rootView;
    }

    public void initChangePass(TextView view){
        if(((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN()){
            view.setEnabled(false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingsActivity) getActivity()).showChangePasswordDialog();
            }
        });
    }
}
