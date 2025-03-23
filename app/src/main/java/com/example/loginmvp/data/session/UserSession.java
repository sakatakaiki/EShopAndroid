package com.example.loginmvp.data.session;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static UserSession instance;

    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public void saveUserSession(Long userId, String role) { // Lưu cả role
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USER_ROLE, role); // Lưu role vào SharedPreferences
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public Long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }

    public String getUserRole() { // Lấy role của user
        return sharedPreferences.getString(KEY_USER_ROLE, "user"); // Mặc định là user
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
