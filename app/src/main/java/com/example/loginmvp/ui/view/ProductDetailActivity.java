package com.example.loginmvp.ui.view;

import static com.example.loginmvp.utils.Constants.BASE_URL;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.loginmvp.R;
import com.example.loginmvp.data.database.AppDatabase;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.presenter.ProductContract;
import com.example.loginmvp.ui.presenter.ProductPresenter;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity implements ProductContract.View {
    private ImageView imgProduct, btnFavorite;
    private MaterialButton btnAddToCart;
    private TextView txtName, txtDescription, txtPrice;
    private ProductPresenter presenter;
    private AppDatabase db;
    private Product currentProduct;
    private TextView txtQuantity;
    private ImageButton btnIncrease, btnDecrease;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Log.d("LAYOUT_DEBUG", "Layout đã được setContentView thành công!");

        imgProduct = findViewById(R.id.imgProductDetail);
        txtName = findViewById(R.id.txtProductNameDetail);
        txtDescription = findViewById(R.id.txtProductDescription);
        txtPrice = findViewById(R.id.txtProductPriceDetail);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        txtQuantity = findViewById(R.id.txtQuantity);
        if (btnFavorite == null) {
            Log.e("FAVORITE_ERROR", "btnFavorite đang null! Kiểm tra ID trong XML.");
        } else {
            Log.d("FAVORITE_DEBUG", "btnFavorite đã được khởi tạo thành công!");
        }


        presenter = new ProductPresenter(this);
        db = AppDatabase.getInstance(this);

        Long productId = getIntent().getLongExtra("product_id", -1);
        if (productId != -1) {
            presenter.loadProductDetails(productId);
            checkFavoriteStatus(productId);
        } else {
            showError("Invalid product ID");
        }

        // Xử lý sự kiện tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });


        btnFavorite.setOnClickListener(v -> {
            Log.d("FAVORITE_CLICK", "Nút favorite đã được bấm!");

            if (currentProduct != null) {
                new Thread(() -> {
                    Product existingProduct = db.productDao().getProductById(currentProduct.getId());

                    runOnUiThread(() -> {
                        if (existingProduct == null) {
                            Product favoriteProduct = new Product(
                                    currentProduct.getId(),
                                    currentProduct.getName(),
                                    currentProduct.getDescription(),
                                    currentProduct.getThumbnail(),
                                    currentProduct.getPrice(),
                                    currentProduct.getRating(),
                                    currentProduct.getSold()
                            );
                            db.productDao().insert(favoriteProduct);
                            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            db.productDao().delete(existingProduct);
                            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        }

                        // **Hiệu ứng scale khi bấm nút**
                        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(
                                btnFavorite,
                                PropertyValuesHolder.ofFloat("scaleX", 1.2f, 1f),
                                PropertyValuesHolder.ofFloat("scaleY", 1.2f, 1f)
                        );
                        scaleAnim.setDuration(200);
                        scaleAnim.start();
                    });
                }).start();
            } else {
                Log.e("FAVORITE_CLICK", "currentProduct = null!");
            }
        });


        btnAddToCart.setOnClickListener(v -> addToCart(productId));




    }

    @Override
    public void showProductDetails(Product product) {
        if (product == null) {
            Log.e("PRODUCT_DEBUG", "Product nhận được là null!");
            return;
        }

        Log.d("PRODUCT_DEBUG", "Sản phẩm nhận được: " + product.getName());

        currentProduct = product;
        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription());
        txtPrice.setText("$" + product.getPrice());

        String imageUrl = BASE_URL + product.getThumbnail();
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(imgProduct);
    }

    private void addToCart(Long productId) {
        if (productId == null || productId == -1) {
            showError("Sản phẩm không hợp lệ");
            return;
        }

        UserSession userSession = UserSession.getInstance(this);
        Long userId = userSession.getUserId(); // Lấy ID của user hiện tại

        if (userId == null) {
            showError("Lỗi: Không tìm thấy thông tin người dùng");
            return;
        }

        presenter.addToCart(userId, productId, quantity);
    }


    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Log.d("DEBUG_MESSAGE", "Thông báo nhận được: " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (message.trim().equalsIgnoreCase("Add Successfully!!")) { // So sánh không phân biệt hoa thường
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        }
    }

    private void checkFavoriteStatus(Long productId) {
        new Thread(() -> {
            Product favoriteProduct = db.productDao().getProductById(productId);
            runOnUiThread(() -> {
                if (favoriteProduct != null) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            });
        }).start();
    }
}
