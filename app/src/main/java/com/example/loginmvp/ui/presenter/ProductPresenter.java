package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.repository.ApiService;
import com.example.loginmvp.data.repository.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View view;
    private ApiService apiService;

    public ProductPresenter(ProductContract.View view) {
        this.view = view;
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public void loadProductDetails(Long productId) {
        apiService.getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showProductDetails(response.body());
                } else {
                    view.showError("Failed to load product details");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                view.showError("Error: " + t.getMessage());
            }
        });
    }
}