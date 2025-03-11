package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.network.AuthResponse;
import com.example.loginmvp.data.network.LoginRequest;
import com.example.loginmvp.data.repository.ApiService;
import com.example.loginmvp.data.repository.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private ApiService apiService;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public void login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onLoginSuccess(response.body());
                } else {
                    view.onLoginError("Login failed. Check your credentials.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                view.onLoginError("Error: " + t.getMessage());
            }
        });
    }
}