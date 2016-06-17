package com.conz13.d.strongpasswordcreator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dillon on 5/23/16.
 */
public class ChangePassDialogFragment extends DialogFragment{
    @BindView(R.id.current_pass_edit_text) EditText mCurrentPass;
    @BindView(R.id.new_pass_edit_text) EditText mNewPass;
    @BindView(R.id.confirm_pass_edit_text) EditText mConfirmPass;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.change_password_dialog, null);
        ButterKnife.bind(this, rootView);

        if(getResources().getBoolean(R.bool.is_right_to_left)){
            setRtlMode();
        }

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
                        save();
                    }
                });
            }
        });

        mConfirmPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    save();
                }
                return false;
            }
        });

        return builder;
    }

    private void save(){
        // Verify the passwords are the same and valid
        // This way if there is an error it won't check the other error cases until it is resolved.
        if(!checkIfEmpty())
            if(checkIfCurrentIsValid())
                if(!checkIfCurrentEqualsNew())
                    if(checkIfNewEqualsConfirm()) {
                        String newPass = mNewPass.getText().toString().trim();
                        ((SettingsActivity) getActivity()).changePassword(newPass);
                        this.dismiss();
                    }

    }

    private void setRtlMode(){
        mCurrentPass.setGravity(Gravity.RIGHT);
        mNewPass.setGravity(Gravity.RIGHT);
        mConfirmPass.setGravity(Gravity.RIGHT);
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
