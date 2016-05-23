package com.conz13.d.strongpasswordcreator;

import android.app.Application;

/**
 * Created by dillon on 5/19/16.
 */
public class MyApplication extends Application {

    private String PASSWORD;
    private boolean SKIPPED_LOGIN;

    public String getPASSWORD(){
        return this.PASSWORD;
    }

    public void setPASSWORD(String password){
        this.PASSWORD = password;
    }

    public boolean getSKIPPED_LOGIN(){ return this.SKIPPED_LOGIN;}

    public void setSKIPPED_LOGIN(boolean skippedLogin){this.SKIPPED_LOGIN = skippedLogin;}
}
