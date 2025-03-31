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
import com.example.loginmvp.data.entities.OrderItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<OrderItem> cartItems = new ArrayList<>();
    private double totalPrice = 0.0;
    private OnTotalPriceUpdatedListener totalPriceListener;

    public interface OnTotalPriceUpdatedListener {
        void onTotalPriceUpdated(double total);
    }

    public CartAdapter(OnTotalPriceUpdatedListener listener) {
        this.totalPriceListener = listener;
    }
    public void setCartItems(List<OrderItem> cartItems) {
        this.cartItems = cartItems;
        calculateTotalPrice();
        notifyDataSetChanged();
    }
    private void calculateTotalPrice() {
        totalPrice = 0.0;
        for (OrderItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        if (totalPriceListener != null) {
            totalPriceListener.onTotalPriceUpdated(totalPrice);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem item = cartItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvPrice.setText("Price: $" + item.getPrice());
        holder.tvQuantity.setText("Amount: " + item.getQuantity());

        DecimalFormat df = new DecimalFormat("0.00");
        holder.tvPrice.setText("$" + df.format(item.getPrice()));

        String imageUrl = BASE_URL + item.getProductThumbnail();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvPrice, tvQuantity;
        ImageView ivThumbnail;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
