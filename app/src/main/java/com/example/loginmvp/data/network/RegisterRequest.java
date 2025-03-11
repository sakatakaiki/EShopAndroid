package com.example.loginmvp.data.network;

public class RegisterRequest {
    private String email;
    private String password;
    private String role = "user";

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
