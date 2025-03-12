package com.example.loginmvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.Category;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.ui.adapter.CategoryAdapter;
import com.example.loginmvp.ui.adapter.ProductAdapter;
import com.example.loginmvp.ui.presenter.HomeContract;
import com.example.loginmvp.ui.presenter.HomePresenter;
import java.util.List;
import androidx.appcompat.widget.SearchView;


public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView recyclerViewCategories, recyclerViewProducts;
    private SearchView searchView;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private HomePresenter presenter;
    private String selectedCategoryName = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));


        setupSearch();

        presenter = new HomePresenter(this);
        presenter.getCategories();  // Lấy danh mục
        presenter.loadProducts();   // Lấy sản phẩm ban đầu

        return view;
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchProducts(query); // Gửi từ khóa tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    presenter.loadProducts(); // Nếu tìm kiếm trống, hiển thị lại tất cả sản phẩm
                }
                return false;
            }
        });
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter = new CategoryAdapter(categories, categoryName -> {
            selectedCategoryName = categoryName;
            presenter.getProductsByCategory(categoryName); // Gọi API bằng categoryName
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
    }


    @Override
    public void showProducts(List<Product> products) {
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(products);
            recyclerViewProducts.setAdapter(productAdapter);
        } else {
            productAdapter.updateData(products); // Cập nhật sản phẩm mà không tạo lại adapter
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
