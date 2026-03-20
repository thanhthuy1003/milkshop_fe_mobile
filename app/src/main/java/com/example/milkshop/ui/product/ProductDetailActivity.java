package com.example.milkshop.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        loadReviews();

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
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        double price = getIntent().getDoubleExtra("PRODUCT_PRICE", 0);
        String desc = getIntent().getStringExtra("PRODUCT_DESC");
        int qty = getIntent().getIntExtra("PRODUCT_QTY", 0);

        tvName.setText(name);
        tvPrice.setText(String.format("%,.0fđ", price));
        tvDescription.setText(desc != null ? desc : "Không có mô tả cho sản phẩm này.");
        tvQuantity.setText(String.valueOf(qty));
    }

    private void loadReviews() {
        String productId = getIntent().getStringExtra("PRODUCT_ID");
        if (productId != null) {
            RetrofitClient.getApiService().getProductReviews(productId, 1, 10).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Logic hiển thị đánh giá (Sẽ cập nhật UI RecyclerView nếu cần)
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Xử lý lỗi
                }
            });
        }
    }
}
