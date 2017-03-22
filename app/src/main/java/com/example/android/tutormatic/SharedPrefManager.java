package com.example.android.tutormatic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mujahid on 2/19/2017.
 */
public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String Mypref = "MyPref";
    private static final String key_user = "name";
    private static final String key_userType = "usertype";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean login(String name, String userType) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_user, name);
        editor.putString(key_userType, userType);
        editor.commit();
        editor.apply();
        return true;
    }

    public boolean isLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(key_user, null) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void logOut() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();


    }

    public String getUserName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_user, null);

    }

    public String getUserType() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_userType, null);
    }

}
