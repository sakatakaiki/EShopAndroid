package com.example.loginmvp.ui.presenter;

import com.example.loginmvp.data.entities.Category;
import com.example.loginmvp.data.entities.Product;
import java.util.List;

public interface HomeContract {
    interface View {
        void showProducts(List<Product> products);
        void showCategories(List<Category> categories);
        void showError(String message);
    }

    interface Presenter {
        void loadProducts();
        void getCategories();
        void getProductsByCategory(String categoryName);
        void searchProducts(String keyword);
    }

}
