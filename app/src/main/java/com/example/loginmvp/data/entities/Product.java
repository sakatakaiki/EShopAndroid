package com.example.loginmvp.data.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private double price;

    public Product(Long id, String name, String description, String thumbnail, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getThumbnail() { return thumbnail; }
    public double getPrice() { return price; }
}