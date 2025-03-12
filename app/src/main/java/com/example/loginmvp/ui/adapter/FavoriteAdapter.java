package com.example.loginmvp.ui.adapter;

import static com.example.loginmvp.utils.Constants.BASE_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.Product;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Product> favoriteList;

    public FavoriteAdapter(List<Product> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = favoriteList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText("$" + product.getPrice());
        String imageUrl = BASE_URL + product.getThumbnail();

        Glide.with(holder.imgThumbnail.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)  // Ảnh hiển thị trong lúc tải
                .error(R.drawable.error_image)        // Ảnh lỗi nếu không tải được
                .into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtName, txtPrice;

        ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}
