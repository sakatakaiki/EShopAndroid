package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.repository.ApiService;
import com.example.loginmvp.data.repository.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private ApiService apiService;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public void loadProducts() {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showProducts(response.body()); // Trả về danh sách sản phẩm
                } else {
                    view.showError("Failed to load products");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                view.showError("Error: " + t.getMessage());
            }
        });
    }

}