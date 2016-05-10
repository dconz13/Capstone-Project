package com.conz13.d.strongpasswordcreator;

import android.app.Dialog;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dillon on 5/9/16.
 */
public class SaveDialogFragment extends DialogFragment {
    private ArrayList<String> mPasswordWords;
    private EditText mPasswordEditText;
    private EditText mHeaderEditText;
    private EditText mUsernameEditText;
    private EditText mAddInfoEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.save_password_dialog_layout,null);
        mPasswordEditText = (EditText) rootView.findViewById(R.id.save_password_edit_text);
        mHeaderEditText = (EditText) rootView.findViewById(R.id.save_header_edit_text);
        mUsernameEditText = (EditText) rootView.findViewById(R.id.save_username_edit_text);
        mAddInfoEditText = (EditText) rootView.findViewById(R.id.save_add_info_edit_text);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.save_dialog_title))
                .setView(rootView)
                .setPositiveButton(getString(R.string.save_dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // submit the password for entry
                        ((MainActivity)getActivity()).onSavePositiveClick(buildContentValues());
                    }
                })
                .setNegativeButton(getString(R.string.save_dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                        ((MainActivity)getActivity()).onSaveNegativeClick();
                    }
                });

        setPasswordString();

        return builder.create();
    }

    private ContentValues buildContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PasswordContract.PasswordEntry.HEADER_TITLE, mHeaderEditText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.USERNAME, mUsernameEditText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.PASSWORD, mPasswordEditText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.ADD_INFO, mAddInfoEditText.getText().toString());

        return contentValues;
    }

    private void setPasswordString(){
        Bundle bundle = getArguments();

        if(bundle.containsKey(getString(R.string.save_dialog_password_key))){
            mPasswordWords = bundle.getStringArrayList(getString(R.string.save_dialog_password_key));
            if(null != mPasswordWords) {
                String result = "";
                for(String word : mPasswordWords){
                    result = result.concat(word + " ");
                }
                result = result.trim();
                mPasswordEditText.setText(result);
            }
        }
    }
}
