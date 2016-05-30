package com.conz13.d.strongpasswordcreator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by dillon on 5/23/16.
 */
public class ChangePassDialogFragment extends DialogFragment{
    private EditText mCurrentPass;
    private EditText mNewPass;
    private EditText mConfirmPass;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.change_password_dialog, null);
        mCurrentPass = (EditText) rootView.findViewById(R.id.current_pass_edit_text);
        mNewPass = (EditText) rootView.findViewById(R.id.new_pass_edit_text);
        mConfirmPass = (EditText) rootView.findViewById(R.id.confirm_pass_edit_text);

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.change_password_title))
                .setView(rootView)
                .setPositiveButton(getString(R.string.change_password_positive), null)
                .setNegativeButton(getString(R.string.change_password_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                //negativeButton.setTextColor(Color.BLACK);
                //positiveButton.setTextColor(Color.BLACK);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Verify the passwords are the same and valid
                        if(!checkIfEmpty() && checkIfCurrentIsValid()
                                && !checkIfCurrentEqualsNew() && checkIfNewEqualsConfirm()){
                            String newPass = mNewPass.getText().toString().trim();
                            ((SettingsActivity)getActivity()).changePassword(newPass);
                            builder.dismiss();
                        }
                    }
                });
            }
        });

        return builder;
    }

    private boolean checkIfCurrentIsValid(){
        String currentPass = mCurrentPass.getText().toString().trim();
        String appPass = ((MyApplication)getActivity().getApplication()).getPASSWORD();
        if(!currentPass.equals(appPass)){
            mCurrentPass.setError(getString(R.string.change_password_current_is_wrong));
            return false;
        }
        return true;
    }

    private boolean checkIfCurrentEqualsNew(){
        String currentPass = mCurrentPass.getText().toString().trim();
        String newPass = mNewPass.getText().toString().trim();
        if(currentPass.equals(newPass)){
            mNewPass.setError(getString(R.string.change_password_current_equals_new));
        }
        return currentPass.equals(newPass);
    }

    private boolean checkIfEmpty(){
        String currentPass = mCurrentPass.getText().toString().trim();
        String newPass = mNewPass.getText().toString().trim();
        String confirmPass = mConfirmPass.getText().toString().trim();
        boolean currentPassFlag = false;
        boolean newPassFlag = false;
        boolean confirmPassFlag = false;

        if(currentPass.isEmpty()){
            mCurrentPass.setError(getString(R.string.change_password_empty));
            currentPassFlag = true;
        }
        if(newPass.isEmpty()){
            mNewPass.setError(getString(R.string.change_password_empty));
            newPassFlag = true;
        }
        if(confirmPass.isEmpty()){
            mConfirmPass.setError(getString(R.string.change_password_empty));
            confirmPassFlag = true;
        }
        if(currentPassFlag || newPassFlag || confirmPassFlag) {
            return true;
        }
        return false;
    }

    private boolean checkIfNewEqualsConfirm(){
        String newPass = mNewPass.getText().toString().trim();
        String confirmPass = mConfirmPass.getText().toString().trim();
        if(!newPass.equals(confirmPass)){
            mConfirmPass.setError(getString(R.string.change_password_confirm_pass_is_wrong));
        }

        return newPass.equals(confirmPass);
    }
}
