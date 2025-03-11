package com.example.loginmvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.ui.adapter.ProductAdapter;
import com.example.loginmvp.ui.presenter.HomeContract;
import com.example.loginmvp.ui.presenter.HomePresenter;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 cột
        recyclerView.setHasFixedSize(true); // Tối ưu hiệu suất
        recyclerView.setItemViewCacheSize(20);

        presenter = new HomePresenter(this);
        presenter.loadProducts(); // Gọi API để lấy danh sách sản phẩm

        return view;
    }


    @Override
    public void showProducts(List<Product> products) {
        adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
