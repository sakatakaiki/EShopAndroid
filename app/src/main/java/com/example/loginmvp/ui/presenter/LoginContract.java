package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.network.AuthResponse;

public interface LoginContract {
    interface View {
        void onLoginSuccess(AuthResponse response);
        void onLoginError(String message);
    }

    interface Presenter {
        void login(String email, String password);
    }
}