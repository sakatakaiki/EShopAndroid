package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Product;

public interface ProductContract {
    interface View {
        void showProductDetails(Product product);
        void showError(String message);
    }

    interface Presenter {
        void loadProductDetails(Long productId);
    }
}