package com.example.loginmvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.Category;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.adapter.CategoryAdapter;
import com.example.loginmvp.ui.adapter.ProductAdapter;
import com.example.loginmvp.ui.adapter.SliderAdapter;
import com.example.loginmvp.ui.presenter.HomeContract;
import com.example.loginmvp.ui.presenter.HomePresenter;
import com.example.loginmvp.ui.view.CartActivity;
import com.example.loginmvp.ui.view.LoginActivity;
import com.example.loginmvp.ui.view.ProductDetailActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;


public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView recyclerViewCategories, recyclerViewProducts;
    private SearchView searchView;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private HomePresenter presenter;
    private ImageView imgCart;
    private String selectedCategoryName = null;
    private ViewPager2 viewPagerSlider;
    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable;
    private int currentPage = 0;
    private List<Integer> imageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imgCart = view.findViewById(R.id.imgCart);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProducts.setLayoutManager(layoutManager);
        recyclerViewProducts.setNestedScrollingEnabled(false);
        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        recyclerViewProducts.setItemViewCacheSize(20);
        recyclerViewProducts.setHasFixedSize(true);



        // Danh sách ảnh
        imageList = Arrays.asList(R.drawable.slider_1, R.drawable.slider_2, R.drawable.slider_3);

        // Khởi tạo Adapter
        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), imageList);
        viewPagerSlider.setAdapter(sliderAdapter);


        // Auto chuyển slide
        autoSlideImages();



        setupSearch();
        recyclerViewProducts.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProducts.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_slide_left));

        presenter = new HomePresenter(this);
        presenter.getCategories();  // Lấy danh mục
        presenter.loadProducts();   // Lấy sản phẩm ban đầu

        imgCart.setOnClickListener(v -> {
            if (UserSession.getInstance(getContext()).isLoggedIn()) {
                startActivity(new Intent(getContext(), CartActivity.class));
            } else {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void autoSlideImages() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (currentPage == imageList.size()) currentPage = 0;
                            viewPagerSlider.setCurrentItem(currentPage++, true);
                        });
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
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


//    @Override
//    public void showProducts(List<Product> products) {
//        if (productAdapter == null) {
//            productAdapter = new ProductAdapter(products);
//            recyclerViewProducts.setAdapter(productAdapter);
//        } else {
//            productAdapter.updateData(products); // Cập nhật sản phẩm mà không tạo lại adapter
//        }
//    }

    @Override
    public void showProducts(List<Product> products) {
        if (products == null || products.isEmpty()) return;

        // Tạo danh sách chỉ lấy 20 sản phẩm đầu tiên
        List<Product> visibleProducts = new ArrayList<>(products.subList(0, Math.min(20, products.size())));

        if (productAdapter == null) {
            productAdapter = new ProductAdapter(visibleProducts);
            recyclerViewProducts.setAdapter(productAdapter);
        } else {
            productAdapter.updateData(visibleProducts);
        }

        // Load tiếp sản phẩm còn lại sau 1 khoảng delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> loadMoreProducts(products), 500);
    }

    private void loadMoreProducts(List<Product> allProducts) {
        if (allProducts.size() > 20) {
            List<Product> remainingProducts = allProducts.subList(20, allProducts.size());
            productAdapter.addMoreProducts(remainingProducts);
        }
    }



    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }
}
