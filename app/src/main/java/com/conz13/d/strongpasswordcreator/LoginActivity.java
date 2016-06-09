package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.sqlcipher.database.SQLiteDatabase;


/**
 * Created by dillon on 5/17/16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean isRtl = getResources().getBoolean(R.bool.is_right_to_left);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.login_activity_layout);
        EditText editText = (EditText) findViewById(R.id.login_edit_text);
        if(isRtl){
            editText.setGravity(Gravity.RIGHT);
        }
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    login(v);
                }
                return false;
            }
        });
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
                ((MyApplication) getApplication()).setSKIPPED_LOGIN(false);

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(R.id.login_button));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.login_button));
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

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
        ((MyApplication)getApplication()).setPASSWORD("");
        ((MyApplication)getApplication()).setSKIPPED_LOGIN(true);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(R.id.login_skip_button));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.login_skip_button));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

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
