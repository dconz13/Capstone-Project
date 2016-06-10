package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by dillon on 6/10/16.
 */
public class AboutActivity extends AppCompatActivity {
    private static final String LOG_TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new AboutFragment(), getString(R.string.about_fragment_tag))
                    .commit();
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        } catch(NullPointerException e){
            Log.e(LOG_TAG, e.toString());
        }
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
