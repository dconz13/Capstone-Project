package com.conz13.d.strongpasswordcreator;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by dillon on 4/19/16.
 *
 * TODO: Create based on the google guidelines for an AlertDialog using custom layout
 * reference link: http://developer.android.com/guide/topics/ui/dialogs.html
 */
public class PasswordPromptDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.alert_dialog_title))
                .setView(inflater.inflate(R.layout.alert_dialog_layout,null))
                .setPositiveButton(getString(R.string.alert_dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // submit the password for confirmation that it is correct
                        ((MainActivity)getActivity()).onPositiveClick();
                    }
                })
                .setNegativeButton(getString(R.string.alert_dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                        ((MainActivity)getActivity()).onNegativeClick();
                    }
                });

        return builder.create();
    }

}
