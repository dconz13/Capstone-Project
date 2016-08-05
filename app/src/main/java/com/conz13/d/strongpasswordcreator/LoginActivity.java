package com.conz13.d.strongpasswordcreator;

import android.content.Intent;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.sqlcipher.database.SQLiteDatabase;


/**
 * Created by dillon on 5/17/16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
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
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView adView = (AdView) findViewById(R.id.login_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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

                Utility.sendAnalytics(this, getString(R.string.login_button), getString(R.string.login_button), "button");

                editText.setText("");

                startActivity(intent);
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

        Utility.sendAnalytics(this, getString(R.string.login_skip_button), getString(R.string.login_skip_button), "button");
        editText.setText("");
        startActivity(intent);
    }
}
