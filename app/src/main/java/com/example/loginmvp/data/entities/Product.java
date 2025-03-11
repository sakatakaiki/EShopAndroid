package com.example.loginmvp.data.entities;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private double price;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getThumbnail() { return thumbnail; }
    public double getPrice() { return price; }
}