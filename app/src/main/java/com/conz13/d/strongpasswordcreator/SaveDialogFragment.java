package com.conz13.d.strongpasswordcreator;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dillon on 5/9/16.
 */
public class SaveDialogFragment extends DialogFragment {
    @BindView(R.id.save_password_edit_text) EditText mPasswordEditText;
    @BindView(R.id.save_header_edit_text) EditText mHeaderEditText;
    @BindView(R.id.save_username_edit_text) EditText mUsernameEditText;
    @BindView(R.id.save_add_info_edit_text) EditText mAddInfoEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.save_password_dialog_layout,null);
        ButterKnife.bind(this, rootView);

        if(getContext().getResources().getBoolean(R.bool.is_right_to_left)){
            setRtlMode();
        }

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.save_dialog_title))
                .setView(rootView)
                .setPositiveButton(getString(R.string.save_dialog_positive), null)
                .setNegativeButton(getString(R.string.save_dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                        ((MainActivity)getActivity()).onSaveNegativeClick();
                    }
                })
                .create();

        // Use View.OnClickListener instead of DialogInterface.OnClickListener so the view can validate before closing
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                });
            }
        });

        mAddInfoEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    save();
                }
                return false;
            }
        });

        setPasswordString();

        return builder;
    }

    private void save(){
        // Check header and password are not empty
        if(checkHeaderAndPassword()){
            // Check if it exists first
            String password = ((MyApplication)getActivity().getApplication()).getPASSWORD();
            if(Utility.checkIfEntryExists(getContext(), password, mHeaderEditText.getText().toString())){
                mHeaderEditText.setError(getString(R.string.save_dialog_header_not_unique));
            }else {
                ((MainActivity) getActivity()).onSavePositiveClick(buildContentValues());
                this.dismiss();
            }
        }
    }

    // Setting it to right works better than end in this case
    private void setRtlMode(){
        mPasswordEditText.setGravity(Gravity.RIGHT);
        mHeaderEditText.setGravity(Gravity.RIGHT);
        mUsernameEditText.setGravity(Gravity.RIGHT);
        mAddInfoEditText.setGravity(Gravity.RIGHT);
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
        ArrayList<String> passwordWords;

        if(bundle.containsKey(getString(R.string.save_dialog_password_key))){
            passwordWords = bundle.getStringArrayList(getString(R.string.save_dialog_password_key));
            if(null != passwordWords) {
                String result = "";
                for(String word : passwordWords){
                    result = result.concat(word + " ");
                }
                result = result.trim();
                mPasswordEditText.setText(result);
            }
        }
    }

    private boolean checkHeaderAndPassword(){
        boolean headerFlag = false;
        boolean passwordFlag = false;
        if(mHeaderEditText.getText().toString().isEmpty()){
            mHeaderEditText.setError(getString(R.string.save_dialog_header_empty));
            headerFlag = true;
        }
        if(mPasswordEditText.getText().toString().isEmpty()){
            mPasswordEditText.setError(getString(R.string.save_dialog_password_empty));
            passwordFlag = true;
        }
        // if either flag is tripped it will return false and prevent the view from closing
        if(headerFlag || passwordFlag){
            return false;
        }
        return true;
    }
}
