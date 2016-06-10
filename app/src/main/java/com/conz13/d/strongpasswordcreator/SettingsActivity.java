package com.conz13.d.strongpasswordcreator;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by dillon on 4/17/16.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
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

    public void showChangePasswordDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(getString(R.string.change_password_dialog_tag));
        if(prev != null){
            ft.remove(prev);
        }
        ChangePassDialogFragment dialogFragment = new ChangePassDialogFragment();
        dialogFragment.show(ft, getString(R.string.change_password_dialog_tag));
    }

    public void changePassword(String newPass){
        if(!((MyApplication) getApplication()).getSKIPPED_LOGIN()) {
            String currentPass = ((MyApplication) getApplication()).getPASSWORD();
            SQLiteDatabase db = new PasswordDbHelper(this).getReadableDatabase(currentPass);
            db.changePassword(newPass);
            db.close();
            ((MyApplication) getApplication()).setPASSWORD(newPass);
        }
    }
}
