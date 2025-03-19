package com.example.loginmvp.data.entities;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private double price;
    private String productName;

    public OrderItem(Long orderId, Long productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }
}