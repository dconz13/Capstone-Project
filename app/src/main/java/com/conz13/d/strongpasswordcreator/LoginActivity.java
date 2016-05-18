package com.conz13.d.strongpasswordcreator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;
import com.conz13.d.strongpasswordcreator.data.PasswordProvider;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by dillon on 5/17/16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.login_activity_layout);
    }

    public void setPasswordTextVisibility(View view) {
        View parentView = view.getRootView();
        EditText editText = (EditText) parentView.findViewById(R.id.login_edit_text);
        CheckBox checkBox = (CheckBox) view;

        if(checkBox.isChecked()){
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setSelection(editText.getText().length());
        }else{
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setSelection(editText.getText().length());
        }
    }

    public void login(View view) {
        EditText editText = (EditText) view.getRootView().findViewById(R.id.login_edit_text);
        if(!editText.getText().toString().isEmpty()){
            String password = editText.getText().toString();
            if(verifyPassword(password)){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(getString(R.string.password_bundle_key), false);

                editText.setText("");

                startActivity(intent);
            }
        } else {
            editText.setError(getString(R.string.login_password_empty_error));
        }
    }

    private boolean verifyPassword(String password){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        PasswordDbHelper dbHelper = new PasswordDbHelper(this);
        try {
            dbHelper.getReadableDatabase(password);
            sharedPreferences.edit()
                    .putString(getString(R.string.password_bundle_key), password)
                    .apply();
            return true;
        } catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        } finally{
            dbHelper.close();
        }

    }

    public void skip(View view) {
        EditText editText = (EditText) view.getRootView().findViewById(R.id.login_edit_text);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.password_bundle_key), true);
        editText.setText("");
        startActivity(intent);
    }
}
