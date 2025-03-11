package com.example.loginmvp.ui.presenter;

public interface RegisterContract {
    interface View {
        void onRegisterSuccess();
        void onRegisterError(String message);
    }

    interface Presenter {
        void register(String email, String password);
    }
}
