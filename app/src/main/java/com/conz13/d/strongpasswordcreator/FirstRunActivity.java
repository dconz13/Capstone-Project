package com.conz13.d.strongpasswordcreator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.tutorial.TutorialPagerAdapter;

/**
 * Created by dillon on 5/17/16.
 */
public class FirstRunActivity extends AppCompatActivity {
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Clear first run flag
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(getString(R.string.first_run_key),false).apply();
        setContentView(R.layout.tutorial_layout);
        mViewPager = (ViewPager) findViewById(R.id.tutorial_view_pager);
        TutorialPagerAdapter adapter = new TutorialPagerAdapter(this, mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tutorial_toolbar);
        setSupportActionBar(toolbar);

    }

    public void createDbLoginPassword(View view){
        Log.d("FirstRunActivity", "button works");
    }

    public void setPasswordTextVisibility(View view) {
        View parentView = view.getRootView();
        EditText editText = (EditText) parentView.findViewById(R.id.create_pass_edit_text);
        CheckBox checkBox = (CheckBox) view;

        if(checkBox.isChecked()){
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setSelection(editText.getText().length());
        }else{
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setSelection(editText.getText().length());
        }
    }

}
