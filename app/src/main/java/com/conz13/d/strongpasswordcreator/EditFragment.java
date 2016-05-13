package com.conz13.d.strongpasswordcreator;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

/**
 * Created by dillon on 5/12/16.
 */
public class EditFragment extends Fragment {

    private EditText mHeaderText;
    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mAddInfoText;
    private long ID;

    private String previousHeaderText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_fragment_layout, container, false);

        mHeaderText = (EditText)rootView.findViewById(R.id.edit_header_edit_text);
        mUsernameText = (EditText)rootView.findViewById(R.id.edit_username_edit_text);
        mPasswordText = (EditText)rootView.findViewById(R.id.edit_password_edit_text);
        mAddInfoText = (EditText)rootView.findViewById(R.id.edit_add_info_edit_text);

        initTextFields();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Save item
        if(item.getItemId() == R.id.edit_save_button){
            if(checkHeaderAndPasswordNotEmpty() && !checkHeaderInUse()) {
                if(Utility.updateExistingEntry(getContext(), getContentValues(), ID)){
                    getActivity().onBackPressed();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTextFields(){
        Bundle bundle = getArguments();
        if(bundle != null){
            ID = bundle.getLong(getString(R.string.intent_extra_id));
            mHeaderText.setText(bundle.getString(getString(R.string.intent_extra_header)));
            mUsernameText.setText(bundle.getString(getString(R.string.intent_extra_user)));
            mPasswordText.setText(bundle.getString(getString(R.string.intent_extra_pass)));
            mAddInfoText.setText(bundle.getString(getString(R.string.intent_extra_add_info)));
            // Only set if it hasn't been set before
            if(null == previousHeaderText){
                previousHeaderText = bundle.getString(getString(R.string.intent_extra_header));
            }
        }
    }

    /**
     * Checks to make sure that the header and password fields are filled out
     *
     * @return true if the fields are filled
     */
    private boolean checkHeaderAndPasswordNotEmpty(){
        boolean headerFlag = true;
        boolean passwordFlag = true;
        if(mHeaderText.getText().toString().isEmpty()){
            mHeaderText.setError(getString(R.string.save_dialog_header_empty));
            headerFlag = false;
        }
        if(mPasswordText.getText().toString().isEmpty()){
            mPasswordText.setError(getString(R.string.save_dialog_password_empty));
            passwordFlag = false;
        }
        if(headerFlag || passwordFlag){
            return true;
        }
        return false;
    }

    /**
     * Checks if the header is in use
     * @return true if it is in use
     */
    private boolean checkHeaderInUse(){
        // Only in use if the entry exists and the header is not the same
        if(Utility.checkIfEntryExists(getContext(), mHeaderText.getText().toString()) && !isHeaderSame()){
            mHeaderText.setError(getString(R.string.save_dialog_header_not_unique));
            return true;
        }
        return false;
    }

    /**
     * Checks if the current header is unchanged
     * @return true if header is unchanged
     */

    private boolean isHeaderSame(){
        if(previousHeaderText.equals(mHeaderText.getText().toString())){
            return true;
        }
        return false;
    }

    /**
     * Builds content values from the edit text fields in the fragment
     * @return content values containing the information from the fragment
     */
    private ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PasswordContract.PasswordEntry.HEADER_TITLE, mHeaderText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.USERNAME, mUsernameText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.PASSWORD, mPasswordText.getText().toString());
        contentValues.put(PasswordContract.PasswordEntry.ADD_INFO, mAddInfoText.getText().toString());
        return contentValues;
    }
}
