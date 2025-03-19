package com.example.loginmvp.ui.view;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.View {
    private CartPresenter presenter;
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        presenter = new CartPresenter(this);

        rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter();
        rvCartItems.setAdapter(cartAdapter);

        Button checkoutButton = findViewById(R.id.btnCheckout);
        checkoutButton.setOnClickListener(v -> presenter.checkout(UserSession.getInstance(this).getUserId()));

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
}
