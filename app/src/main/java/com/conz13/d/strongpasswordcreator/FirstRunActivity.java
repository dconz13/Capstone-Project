package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;
import com.conz13.d.strongpasswordcreator.tutorial.TutorialPagerAdapter;
import com.conz13.d.strongpasswordcreator.tutorial.skipAndArrowHider;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.sqlcipher.database.SQLiteDatabase;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by dillon on 5/17/16.
 */
public class FirstRunActivity extends AppCompatActivity implements skipAndArrowHider {
    ViewPager mViewPager;
    FirebaseAnalytics mFirebaseAnalytics;
    Boolean isRtl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);
        SQLiteDatabase.loadLibs(this);

        // TODO: Fix this when viewpager supports RTL
        mViewPager = (ViewPager) findViewById(R.id.tutorial_view_pager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        indicator.setViewPager(mViewPager);

        TutorialPagerAdapter adapter = new TutorialPagerAdapter(this, mViewPager, indicator, this);

        ImageButton nextButton = (ImageButton) findViewById(R.id.tutorial_next_page);
        Button skipButton = (Button) findViewById(R.id.tutorial_skip);
        nextButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));

        initSkipButton(skipButton);
        initNextButton(nextButton);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(R.id.tutorial_view_pager));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Tutorial");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "begin");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle);
    }

    public void createDbLoginPassword(View view){
        EditText editText = (EditText) view.getRootView().findViewById(R.id.create_pass_edit_text);
        isRtl = getResources().getBoolean(R.bool.is_right_to_left);
        if(isRtl){
            editText.setGravity(Gravity.RIGHT);
        }
        if(!editText.getText().toString().isEmpty()){
            String password = editText.getText().toString();
            if(verifyPassword(password)){
                Intent intent = new Intent(this, MainActivity.class);
                ((MyApplication)getApplication()).setPASSWORD(password);
                ((MyApplication) getApplication()).setSKIPPED_LOGIN(false);

                editText.setText("");

                // Clear first run flag
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putBoolean(getString(R.string.first_run_key),false).apply();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(R.id.tutorial_view_pager));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Tutorial");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "finish");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, bundle);

                startActivity(intent);
                this.finish();
            } else {
                editText.setError(getString(R.string.login_password_wrong_error));
            }
        } else {
            editText.setError(getString(R.string.login_password_empty_error));
        }
    }

    private void initSkipButton(Button skipButton){
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3, true);
            }
        });
    }

    private void initNextButton(ImageButton nextButton){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPage = mViewPager.getCurrentItem();
                currPage = ++currPage;
                mViewPager.setCurrentItem(currPage, true);
            }
        });
    }

    @Override
    public void setSkipAndArrowVisibility(int visibility) {
        Button skipButton = (Button) findViewById(R.id.tutorial_skip);
        ImageButton nextButton = (ImageButton) findViewById(R.id.tutorial_next_page);
        skipButton.setVisibility(visibility);
        nextButton.setVisibility(visibility);
    }

    private boolean verifyPassword(String password){
        return new VerifyPasswordTask(this).doInBackground(password);
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
