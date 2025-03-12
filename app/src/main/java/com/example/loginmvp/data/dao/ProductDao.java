package com.example.loginmvp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.loginmvp.data.entities.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);


    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllFavorites();

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductById(Long productId);


}