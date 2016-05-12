package com.conz13.d.strongpasswordcreator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.conz13.d.strongpasswordcreator.data.PasswordContract;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dillon on 4/17/16.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private NavigationView mNavDrawer;

    public static final int HOME = 0;
    public static final int LOCKER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.main_activity_layout);

        // TODO: Add item icons

        mNavDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        setUpNavigationDrawerListener(mNavDrawer);
        mNavDrawer.setCheckedItem(R.id.menu_home);
        // TODO: find out why this causes illegalargumentexception. why is child null view
        //navDrawer.addHeaderView(findViewById(R.id.nav_drawer_header));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        Toolbar tempToolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, tempToolbar,
                R.string.drawer_open,R.string.drawer_close);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }

    // Easy way to update nav menu selected item from fragments in onCreateView
    public void updateNavItemSelected(int tag){
        switch(tag){
            case HOME: {
                mNavDrawer.setCheckedItem(R.id.menu_home);
                break;
            }
            case LOCKER: {
                mNavDrawer.setCheckedItem(R.id.menu_locker);
                break;
            }
            default:{
                break;
            }
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
        final Context context = getApplicationContext();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        launchWordGenerationFragment();
                        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
                            mDrawerLayout.closeDrawers();
                        }
                        return true;
                    case R.id.menu_locker:
                        //showAlertDialog();
                        launchLockerFragment();
                        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
                            mDrawerLayout.closeDrawers();
                        }
                        return true;
                    case R.id.menu_settings:
                        startActivity(new Intent(context, SettingsActivity.class));
                        return true;
                    case R.id.menu_help:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void launchLockerFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content_frame, new LockerFragment(), getString(R.string.locker_fragment_tag))
                .addToBackStack(null)
                .commit();
    }

    private void launchWordGenerationFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content_frame, new WordGenerationFragment(), getString(R.string.generation_fragment_tag))
                .addToBackStack(null)
                .commit();
    }

    private void showAlertDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment previousFrag = getSupportFragmentManager().findFragmentByTag(getString(R.string.alert_dialog_tag));
        if(previousFrag != null){
            ft.remove(previousFrag);
        }
        // Setting this to null allows you to go back with the back button but for dialog fragment it isn't necessary
        // ft.addToBackStack(null);

        new PasswordPromptDialogFragment().show(ft, getString(R.string.alert_dialog_tag));
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

    public void onPositiveClick(){
        // Replace the Word Generation Fragment with the LockerFragment
    }

    public void onNegativeClick(){
        // Switch the highlighted item back to the "Home" entry
        mNavDrawer.setCheckedItem(R.id.menu_home);
    }
    public void onSavePositiveClick(ContentValues contentValues){
        // Save to database
        Uri contentUri = PasswordContract.PasswordEntry.CONTENT_URI;
        this.getContentResolver().insert(contentUri, contentValues);
        // Clear list in fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.generation_fragment_tag));
        ((WordGenerationFragment)fragment).clearList();
    }

    public void onSaveNegativeClick(){
        // Do nothing
    }

    public void setPasswordTextVisibility(View view){
        View parentView = view.getRootView();
        EditText editText = (EditText) parentView.findViewById(R.id.alert_password_entry);
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
