package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Order;
import com.example.loginmvp.data.entities.OrderItem;

import java.util.List;

public interface CartContract {
    interface View {
        void showCartItems(List<OrderItem> orderItems);
        void showMessage(String message);
    }

    interface Presenter {
        void addToCart(Long userId, Long productId, double price);
        void checkout(Long userId);
        void loadCartItems(Long userId);
    }
}
