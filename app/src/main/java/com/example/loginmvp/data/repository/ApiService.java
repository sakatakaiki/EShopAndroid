package com.example.loginmvp.data.repository;

import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.network.AuthResponse;
import com.example.loginmvp.data.network.LoginRequest;
import com.example.loginmvp.data.network.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("/api/products")
    Call<List<Product>> getProducts();

    @GET("/api/products/{id}")
    Call<Product> getProductById(@Path("id") Long id);

}