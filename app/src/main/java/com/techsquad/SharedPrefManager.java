package com.techsquad;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

//here for this class we are using a singleton pattern

class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "cts_user_details";
    private static final String KEY_ACCESS = "cts_access";
    private static final String KEY_REFRESH = "cts_refresh";
    private static final String KEY_ACCESS_CSRF = "cts_access_csrf";
    private static final String KEY_REFRESH_CSRF = "cts_refresh_csrf";
    private static final String KEY_FNAME = "cts_fname";
    private static final String KEY_LNAME = "cts_lname";
    private static final String KEY_PHONE = "cts_phone";
    private static final String KEY_EMAIL = "cts_email";
    private static final String KEY_ID = "cts_id";
    private static final String KEY_FIRST_LOGIN = "cts_first_login";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    boolean getFirstLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_FIRST_LOGIN, true);

    }

    void setKeyFirstLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_LOGIN, false);
        editor.apply();
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    void userLogin(User user, LoginCookies cookies) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS, cookies.getAccessCookie());
        editor.putString(KEY_ACCESS_CSRF, cookies.getCsrfAccessCookie());
        editor.putString(KEY_REFRESH, cookies.getRefreshCookie());
        editor.putString(KEY_REFRESH_CSRF, cookies.getCsrfRefreshCookie());
        editor.putString(KEY_FNAME, user.getfName());
        editor.putString(KEY_LNAME, user.getlName());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_EMAIL, user.getEmail().equals("null") ? "E-Mail Unspecified" : user.getEmail());
        editor.putInt(KEY_ID, user.getId());
        editor.commit();
    }

    void setAccessCookie(Map<String, String> cookieList) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS, cookieList.get(LoginCookies.JWT_ACCESS_NAME));
        editor.putString(KEY_ACCESS_CSRF, cookieList.get(LoginCookies.JWT_ACCESS_CSRF_NAME));
        editor.commit();
    }

    //this method will checker whether user is already logged in or not
    boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, null) != null && sharedPreferences.getString(KEY_ACCESS, null) != null;
    }

    LoginCookies getLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, String> cookieList = new HashMap<>();
        cookieList.put(LoginCookies.JWT_ACCESS_NAME, sharedPreferences.getString(KEY_ACCESS, null));
        cookieList.put(LoginCookies.JWT_REFRESH_NAME, sharedPreferences.getString(KEY_REFRESH, null));
        cookieList.put(LoginCookies.JWT_ACCESS_CSRF_NAME, sharedPreferences.getString(KEY_ACCESS_CSRF, null));
        cookieList.put(LoginCookies.JWT_REFRESH_CSRF_NAME, sharedPreferences.getString(KEY_REFRESH_CSRF, null));
        return new LoginCookies(cookieList);
    }

    //this method will give the logged in user
    User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_FNAME, null),
                sharedPreferences.getString(KEY_LNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getInt(KEY_ID, 0)
        );
    }

    //this method will logout the user
    void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}