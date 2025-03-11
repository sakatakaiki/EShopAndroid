package com.example.loginmvp.ui.view;

import static com.example.loginmvp.utils.Constants.BASE_URL;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.Product;
import com.example.loginmvp.ui.presenter.ProductContract;
import com.example.loginmvp.ui.presenter.ProductPresenter;

public class ProductDetailActivity extends AppCompatActivity implements ProductContract.View {
    private ImageView imgProduct;
    private TextView txtName, txtDescription, txtPrice;
    private ProductPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProductDetail);
        txtName = findViewById(R.id.txtProductNameDetail);
        txtDescription = findViewById(R.id.txtProductDescription);
        txtPrice = findViewById(R.id.txtProductPriceDetail);

        presenter = new ProductPresenter(this);

        Long productId = getIntent().getLongExtra("product_id", -1);
        if (productId != -1) {
            presenter.loadProductDetails(productId);
        } else {
            showError("Invalid product ID");
        }
    }

    @Override
    public void showProductDetails(Product product) {
        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription());
        txtPrice.setText("$" + product.getPrice());

        // Thêm BASE_URL để đảm bảo đường dẫn ảnh đúng
        String imageUrl = BASE_URL + product.getThumbnail();

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)  // Ảnh tạm nếu tải chậm
                .error(R.drawable.error_image)        // Ảnh lỗi nếu không tải được
                .into(imgProduct);
    }


    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
