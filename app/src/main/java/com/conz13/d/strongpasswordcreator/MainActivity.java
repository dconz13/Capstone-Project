package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by dillon on 4/17/16.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private NavigationView mNavDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mNavDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        setUpNavigationDrawerListener(mNavDrawer);
        //find out why this causes illegalargumentexception. why is child null view
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
                    .add(R.id.main_content_frame, new WordGenerationFragment())
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

    private void setUpNavigationDrawerListener(NavigationView navView) {
        final Context context = getApplicationContext();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Log.d("navItemSelected", "menu_home");
                        return true;
                    case R.id.menu_locker:
                        Log.d("navItemSelected", "menu_locker");
                        showAlertDialog();
                        return true;
                    case R.id.menu_settings:
                        startActivity(new Intent(context, SettingsActivity.class));
                        Log.d("navItemSelected", "menu_settings");
                        return true;
                    case R.id.menu_help:
                        Log.d("navItemSelected", "menu_help");
                        return true;
                    default:
                        return false;
                }
            }
        });
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

    public void onPositiveClick(){

    }

    public void onNegativeClick(){
        // Switch the highlighted item back to the "Home" entry
        mNavDrawer.setCheckedItem(R.id.menu_home);
    }
}
