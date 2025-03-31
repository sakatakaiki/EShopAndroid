package com.example.loginmvp.ui.adapter;

import static com.example.loginmvp.utils.Constants.BASE_URL;

import android.content.Intent;
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
import com.example.loginmvp.ui.view.ProductDetailActivity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText("$" + product.getPrice());

        holder.txtSold.setText(product.getSold() + " sold");

        DecimalFormat df = new DecimalFormat("0.0");
        holder.txtRating.setText(df.format(product.getRating()));


        // Đảm bảo đường dẫn ảnh đầy đủ
        String imageUrl = BASE_URL + product.getThumbnail();

        Glide.with(holder.imgThumbnail.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)  // Ảnh hiển thị trong lúc tải
                .error(R.drawable.error_image)        // Ảnh lỗi nếu không tải được
                .into(holder.imgThumbnail);

        // Mở ProductDetailActivity khi click vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId()); // Truyền ID sản phẩm
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // 🆕 Phương thức cập nhật danh sách sản phẩm
    public void updateData(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtSold, txtRating;
        ImageView imgThumbnail;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            imgThumbnail = itemView.findViewById(R.id.imgProduct);
            txtSold = itemView.findViewById(R.id.txtSold);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }

    public void addMoreProducts(List<Product> moreProducts) {
        int startPos = productList.size();
        productList.addAll(moreProducts);
        notifyItemRangeInserted(startPos, moreProducts.size());
    }

}