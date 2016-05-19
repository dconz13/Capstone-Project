package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import net.sqlcipher.database.SQLiteDatabase;


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
                ((MyApplication)getApplication()).setPASSWORD(password);
                intent.putExtra(getString(R.string.skipped_key), false);

                editText.setText("");

                startActivity(intent);
               // this.finish();
            } else {
                editText.setError(getString(R.string.login_password_wrong_error));
            }
        } else {
            editText.setError(getString(R.string.login_password_empty_error));
        }
    }

    private boolean verifyPassword(String password){
        return new VerifyPasswordTask(this).doInBackground(password);
    }

    public void skip(View view) {
        EditText editText = (EditText) view.getRootView().findViewById(R.id.login_edit_text);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.skipped_key), true);
        ((MyApplication)getApplication()).setPASSWORD("");
        editText.setText("");
        startActivity(intent);
        //this.finish();
    }

    private class VerifyPasswordTask extends AsyncTask<String, Void, Boolean> {
        private Context mContext;

        public VerifyPasswordTask(Context context){
            mContext = context;
        }
        @Override
        protected Boolean doInBackground(String... params) {
            PasswordDbHelper dbHelper = new PasswordDbHelper(mContext);
            String password = params[0];

            try {
                dbHelper.getReadableDatabase(password);
                return true;
            } catch (Exception e){
                //Log.e(LOG_TAG, e.getMessage());
                return false;
            } finally{
                dbHelper.close();
            }
        }
    }
}
