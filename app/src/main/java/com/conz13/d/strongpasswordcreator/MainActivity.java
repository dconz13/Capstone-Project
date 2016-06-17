package com.conz13.d.strongpasswordcreator;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

/**
 * Created by dillon on 4/17/16.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private NavigationView mNavDrawer;
    FirebaseAnalytics mFirebaseAnalytics;

    public static final int HOME = 0;
    public static final int LOCKER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mNavDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        setUpNavigationDrawerListener(mNavDrawer);
        mNavDrawer.setCheckedItem(R.id.menu_home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        Toolbar tempToolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, tempToolbar,
                R.string.drawer_open,R.string.drawer_close);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setSupportActionBar(tempToolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } catch(NullPointerException e){
            Log.e(LOG_TAG, e.toString());
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content_frame, new WordGenerationFragment(), getString(R.string.generation_fragment_tag))
                    .commit();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString(getString(R.string.change_language_key),
                getString(R.string.change_language_default));
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, language);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, language);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "languageSetting");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void signOut() {
        boolean skippedFlag = ((MyApplication)getApplication()).getSKIPPED_LOGIN();

        if(skippedFlag){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.sign_in));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.sign_in));
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else{
            final Context context = this;
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.logout_dialog_message))
                    .setPositiveButton(getString(R.string.logout_dalog_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.sign_out));
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.sign_out));
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                            startActivity(new Intent(context, LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.logout_dialog_negative), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {
            signOut();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setUpNavigationDrawerListener(final NavigationView navView) {
        final Context context = this;
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Bundle bundle = new Bundle();

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.home));
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.home));
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "currentScreen");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        launchWordGenerationFragment();
                        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
                            mDrawerLayout.closeDrawers();
                        }
                        return true;
                    case R.id.menu_locker:
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.my_locker));
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.my_locker));
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "currentScreen");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        launchLockerFragment();
                        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
                            mDrawerLayout.closeDrawers();
                        }
                        return true;
                    case R.id.menu_settings:
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.settings));
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.settings));
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "currentScreen");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        startActivity(new Intent(context, SettingsActivity.class));
                        return false;
                    case R.id.menu_about:
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.about_title));
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.about_title));
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "currentScreen");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        startActivity(new Intent(context, AboutActivity.class));
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    private void launchLockerFragment(){
        if(null == getSupportFragmentManager().findFragmentByTag(getString(R.string.locker_fragment_tag))
                || !getSupportFragmentManager().findFragmentByTag(getString(R.string.locker_fragment_tag)).isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_frame, new LockerFragment(), getString(R.string.locker_fragment_tag))
//                    .addToBackStack(null)
                    .commit();
        }
    }

    private void launchWordGenerationFragment(){
        // I think the null check may be unnecessary for this fragment but since it crashed for the
        // other fragment here, I decided to add the check just to be safe from weird cases
        if(null == getSupportFragmentManager().findFragmentByTag(getString(R.string.generation_fragment_tag))
                || !getSupportFragmentManager().findFragmentByTag(getString(R.string.generation_fragment_tag)).isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content_frame, new WordGenerationFragment(), getString(R.string.generation_fragment_tag))
//                    .addToBackStack(null)
                    .commit();
        }
    }

    public void showSaveDialog(ArrayList<String> wordList){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(getString(R.string.save_dialog_tag));
        if(prev != null){
            ft.remove(prev);
        }
        Bundle args = new Bundle();
        args.putStringArrayList(getString(R.string.save_dialog_password_key), wordList);
        SaveDialogFragment saveDialogFragment = new SaveDialogFragment();
        saveDialogFragment.setArguments(args);
        saveDialogFragment.show(ft, getString(R.string.save_dialog_tag));
    }

    public void onSavePositiveClick(ContentValues contentValues){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.generation_fragment_tag));
        int words = ((WordGenerationFragment)fragment).getWordListLength();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.save_dialog_tag) + " # words: " + Integer.toString(words));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,
                getString(R.string.save_dialog_tag) + " # words: " + Integer.toString(words));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "password_length");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        // Save to database
        saveToDatabase(contentValues);
        // Clear list in fragment
        ((WordGenerationFragment)fragment).clearList(WordGenerationFragment.SAVE_BUTTON);
    }

    public void onSaveNegativeClick(){
        // Do nothing
    }

    private void saveToDatabase(ContentValues contentValues){
        // Password for unlocking the db that the user provided at login
        String password = ((MyApplication)getApplication()).getPASSWORD();
        String method = getString(R.string.database_password_method);
        getContentResolver().call(PasswordContract.BASE_CONTENT_URI, method, password, null);
        getContentResolver().insert(PasswordContract.PasswordEntry.CONTENT_URI, contentValues);
    }

}
