package com.conz13.d.strongpasswordcreator;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.conz13.d.strongpasswordcreator.data.PasswordDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by dillon on 4/17/16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
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
