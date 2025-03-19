package com.example.loginmvp.data.entities;

public class User {
    private int id;  // Thêm id
    private String email;
    private String role;

    public User(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

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
