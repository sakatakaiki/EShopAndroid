package com.example.loginmvp.ui.presenter;

import android.util.Log;

import com.example.loginmvp.data.entities.Order;
import com.example.loginmvp.data.entities.OrderItem;
import com.example.loginmvp.data.repository.CartCallback;
import com.example.loginmvp.data.repository.CartRepository;

import java.util.Collections;
import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view;
    private CartRepository cartRepository;

    public CartPresenter(CartContract.View view) {
        this.view = view;
        this.cartRepository = new CartRepository();
    }

    @Override
    public void addToCart(Long userId, Long productId, double price) {
        cartRepository.getPendingOrder(userId, new CartCallback<>() {
            @Override
            public void onResult(Order order) {
                if (order != null) {
                    addOrderItem(order.getId(), productId, price);
                } else {
                    view.showMessage("No order found!");
                }
            }

            @Override
            public void onError(Throwable t) {
                view.showMessage("Error when getting pending order!");
            }
        });
    }


    private void addOrderItem(Long orderId, Long productId, double price) {
        cartRepository.addOrderItem(new OrderItem(orderId, productId, 1, price), new CartCallback<>() {
            @Override
            public void onResult(Void result) {
                view.showMessage("Add Successfully!!");
            }

            @Override
            public void onError(Throwable t) {
                view.showMessage("Error while adding to cart!");
            }
        });
    }

    @Override
    public void checkout(Long userId) {
        cartRepository.checkout(userId, new CartCallback<>() {
            @Override
            public void onResult(Void result) {
                view.showMessage("Checkout Success!");
                view.showCartItems(Collections.emptyList());
                loadCartItems(userId);
            }

            @Override
            public void onError(Throwable t) {
                view.showMessage("Error while checkout!");
            }
        });
    }

    @Override
    public void loadCartItems(Long userId) {
        Log.d("CartPresenter", "Fetching cart items for user: " + userId);
        cartRepository.getCartItems(userId, new CartCallback<List<OrderItem>>() {
            @Override
            public void onResult(List<OrderItem> orderItems) {
                if (orderItems == null || orderItems.isEmpty()) {
                    view.showMessage("Empty Cart");
                    view.showCartItems(Collections.emptyList());
                } else {
                    view.showCartItems(orderItems);
                    view.updateCartTitle(orderItems);
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e("CartPresenter", "Error fetching cart items: " + t.getMessage(), t);
                view.showMessage("Empty Cart");
            }
        });
    }



}
