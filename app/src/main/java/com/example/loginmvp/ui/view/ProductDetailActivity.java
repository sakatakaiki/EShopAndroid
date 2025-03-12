package com.example.loginmvp.ui.view;

import static com.example.loginmvp.utils.Constants.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.loginmvp.R;
import com.example.loginmvp.data.database.AppDatabase;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.ui.presenter.ProductContract;
import com.example.loginmvp.ui.presenter.ProductPresenter;

public class ProductDetailActivity extends AppCompatActivity implements ProductContract.View {
    private ImageView imgProduct, btnFavorite;
    private TextView txtName, txtDescription, txtPrice;
    private ProductPresenter presenter;
    private AppDatabase db;
    private Product currentProduct;

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

        btnFavorite.setOnClickListener(v -> {
            Log.d("FAVORITE_CLICK", "Nút favorite đã được bấm!");

            if (currentProduct != null) {
                new Thread(() -> {
                    Product existingProduct = db.productDao().getProductById(currentProduct.getId());

                    if (existingProduct == null) {
                        db.productDao().insert(currentProduct);
                        runOnUiThread(() -> {
                            Log.d("FAVORITE_CLICK", "Sản phẩm đã được thêm vào Room Database!");
                            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        db.productDao().delete(existingProduct);
                        runOnUiThread(() -> {
                            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            } else {
                Log.e("FAVORITE_CLICK", "currentProduct = null!");
            }
        });



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



    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
