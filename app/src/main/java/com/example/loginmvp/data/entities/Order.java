package com.example.loginmvp.data.entities;

public class Order {
    private Long id;
    private String code;
    private String status;
    private Long userId;

    public Order(Long userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }
}

