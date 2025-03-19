package com.example.loginmvp.data.repository;

import com.example.loginmvp.data.entities.Order;
import com.example.loginmvp.data.entities.OrderItem;
import com.example.loginmvp.data.repository.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private ApiService apiService;

    public CartRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public void getPendingOrder(Long userId, CartCallback<Order> callback) {
        apiService.getPendingOrder(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                callback.onResult(response.body());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



    public void addOrderItem(OrderItem orderItem, CartCallback<Void> callback) {
        apiService.addOrderItem(orderItem).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onResult(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void checkout(Long userId, CartCallback<Void> callback) {
        apiService.checkout(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResult(null);
                } else {
                    callback.onError(new Throwable("Error when checkout"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



    public void getCartItems(Long userId, CartCallback<List<OrderItem>> callback) {
        apiService.getCartItems(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onError(new Throwable("Error when get cart item"));
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}