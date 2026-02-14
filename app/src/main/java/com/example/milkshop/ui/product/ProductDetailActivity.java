package com.example.milkshop.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDescription, tvQuantity;
    private MaterialButton btnAddToCart;
    private boolean isGuest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", true);

        initViews();
        displayProductInfo();

        btnAddToCart.setOnClickListener(v -> {
            if (isGuest) {
                Toast.makeText(this, "Vui lòng đăng nhập để mua hàng!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        imgProduct = findViewById(R.id.imgProductDetail);
        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvQuantity = findViewById(R.id.tvDetailQuantity);
        btnAddToCart = findViewById(R.id.btnDetailAddToCart);
    }

    private void displayProductInfo() {
        // Nhận dữ liệu từ Intent (trong thực tế có thể gọi API theo ID)
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        double price = getIntent().getDoubleExtra("PRODUCT_PRICE", 0);
        String desc = getIntent().getStringExtra("PRODUCT_DESC");
        int qty = getIntent().getIntExtra("PRODUCT_QTY", 0);

        tvName.setText(name);
        tvPrice.setText(String.format("%,.0fđ", price));
        tvDescription.setText(desc != null ? desc : "Không có mô tả cho sản phẩm này.");
        tvQuantity.setText(String.valueOf(qty));
    }
}
