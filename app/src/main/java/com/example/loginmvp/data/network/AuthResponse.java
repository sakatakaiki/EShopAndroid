package com.example.loginmvp.data.network;

import com.example.loginmvp.data.entities.User;

public class AuthResponse {
    private int id;
    private String email;
    private String role;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
