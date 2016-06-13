package com.conz13.d.strongpasswordcreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by dillon on 5/12/16.
 */
public class EditActivity extends AppCompatActivity {
    private final static String LOG_TAG = EditActivity.class.getSimpleName();
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_activity_toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        } catch(NullPointerException e){
            Log.e(LOG_TAG, e.toString());
        }

        Bundle extras = getIntent().getExtras();

        if(savedInstanceState == null){
            Fragment editFragment = new EditFragment();
            editFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.edit_content_frame, editFragment, getString(R.string.edit_fragment_tag))
                    .commit();
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "EditMode");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "EditMode");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "currentScreen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void setResult(CharSequence message){
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.edit_snackbar_key), message);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
