package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Category;
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

    public void getCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showCategories(response.body()); // Gửi danh mục đến View
                } else {
                    view.showError("Không thể tải danh mục");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                view.showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getProductsByCategory(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            view.showError("Danh mục không hợp lệ");
            return;
        }

        apiService.getProductsByCategory(categoryName).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showProducts(response.body()); // Hiển thị sản phẩm theo danh mục
                } else {
                    view.showError("Không có sản phẩm trong danh mục này");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                view.showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    @Override
    public void loadProducts() {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showProducts(response.body());
                } else {
                    view.showError("Không thể tải sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                view.showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            view.showError("Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        apiService.searchProducts(keyword).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.showProducts(response.body()); // Hiển thị danh sách sản phẩm tìm kiếm được
                } else {
                    view.showError("Không tìm thấy sản phẩm nào");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                view.showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
