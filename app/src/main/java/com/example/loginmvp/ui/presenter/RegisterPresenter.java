package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.network.AuthResponse;
import com.example.loginmvp.data.network.RegisterRequest;
import com.example.loginmvp.data.repository.ApiService;
import com.example.loginmvp.data.repository.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View view;
    private ApiService apiService;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public void register(String email, String password) {
        RegisterRequest request = new RegisterRequest(email, password);

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onRegisterSuccess();
                } else {
                    view.onRegisterError("Registration failed.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                view.onRegisterError("Error: " + t.getMessage());
            }
        });
    }
}