package com.example.loginmvp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.loginmvp.R;
import com.example.loginmvp.data.database.AppDatabase;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.ui.adapter.FavoriteAdapter;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        db = AppDatabase.getInstance(getContext());
        loadFavorites();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites(); // Cập nhật danh sách mỗi khi quay lại màn hình này
    }


    private void loadFavorites() {
        new Thread(() -> {
            List<Product> favorites = db.productDao().getAllFavorites();
            for (Product p : favorites) {
                Log.d("FAVORITES", "ID: " + p.getId() + ", Name: " + p.getName());
            }
            requireActivity().runOnUiThread(() -> {
                if (!favorites.isEmpty()) {
                    adapter = new FavoriteAdapter(favorites);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không có sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


}
