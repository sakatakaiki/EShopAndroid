package com.example.loginmvp.ui.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.OrderItem;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.adapter.CartAdapter;
import com.example.loginmvp.ui.presenter.CartContract;
import com.example.loginmvp.ui.presenter.CartPresenter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.View, CartAdapter.OnTotalPriceUpdatedListener {
    private CartPresenter presenter;
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice, tvCartTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        presenter = new CartPresenter(this);

        rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCartTitle = findViewById(R.id.tvCartTitle);
        cartAdapter = new CartAdapter(this);
        rvCartItems.setAdapter(cartAdapter);

        MaterialButton checkoutButton = findViewById(R.id.btnCheckout);
        checkoutButton.setOnClickListener(v -> presenter.checkout(UserSession.getInstance(this).getUserId()));

        ImageButton btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.btnChat).setOnClickListener(v -> {
            Log.d("CartActivity", "Navigating to HomeActivity with openChat = true");
            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
            intent.putExtra("openChat", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Gọi presenter để lấy danh sách sản phẩm trong giỏ hàng
        presenter.loadCartItems(UserSession.getInstance(this).getUserId());

    }

    @Override
    public void showCartItems(List<OrderItem> orderItems) {
        Log.d("CartActivity", "showCartItems() called");
        Log.d("CartActivity", "Displaying " + orderItems.size() + " items in cart");
        cartAdapter.setCartItems(orderItems);
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTotalPriceUpdated(double total) {
        Log.d("CartActivity", "Total price updated: $" + total);
        tvTotalPrice.setText("Total: $" + String.format("%.2f", total)); // 🆕 Cập nhật giá trị TextView
    }

    @Override
    public void updateCartTitle(List<OrderItem> cartItems) {
        int totalItems = 0;
        for (OrderItem item : cartItems) {
            totalItems += item.getQuantity();
        }
        tvCartTitle.setText("YOUR CART (" + totalItems + ")");
        cartAdapter.notifyDataSetChanged();
    }
}
