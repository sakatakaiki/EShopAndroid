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
    private float rating;
    private int sold;

    public Product(Long id, String name, String description, String thumbnail, double price, float rating, int sold) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
        this.rating = rating;
        this.sold = sold;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getThumbnail() { return thumbnail; }
    public double getPrice() { return price; }
    public float getRating() {
        return rating;
    }

    public int getSold() {
        return sold;
    }
}