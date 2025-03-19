package com.example.loginmvp.data.repository;

import com.example.loginmvp.data.entities.Order;
import com.example.loginmvp.data.entities.OrderItem;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.entities.Category;
import com.example.loginmvp.data.network.AuthResponse;
import com.example.loginmvp.data.network.LoginRequest;
import com.example.loginmvp.data.network.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("/api/products")
    Call<List<Product>> getProducts();

    @GET("/api/products/{id}")
    Call<Product> getProductById(@Path("id") Long id);

    @GET("/api/categories")
    Call<List<Category>> getCategories();



    @GET("/api/products/category/{categoryName}")
    Call<List<Product>> getProductsByCategory(@Path("categoryName") String categoryName);

    @GET("/api/products/search/{keyword}")
    Call<List<Product>> searchProducts(@Path("keyword") String keyword);

    @GET("/api/orders/pending/{userId}")
    Call<Order> getPendingOrder(@Path("userId") Long userId);

    @POST("/api/orders")
    Call<Order> createOrder(@Body Order order);

    @POST("/api/order-items")
    Call<Void> addOrderItem(@Body OrderItem orderItem);

    @POST("/api/orders/checkout/{userId}")
    Call<Void> checkout(@Path("userId") Long userId);

    @POST("/api/orders/addToCart/{userId}/{productId}")
    Call<Order> addToCart(
            @Path("userId") Long userId,
            @Path("productId") Long productId,
            @Query("quantity") int quantity
    );



    @GET("/api/order-items/cart/{userId}")
    Call<List<OrderItem>> getCartItems(@Path("userId") Long userId);


}