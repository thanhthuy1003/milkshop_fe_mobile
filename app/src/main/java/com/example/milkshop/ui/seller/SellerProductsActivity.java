package com.example.milkshop.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.Product;
import com.example.milkshop.ui.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private ImageView btnBack;
    private Button btnAddMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);

        initViews();
        setupRecyclerView();

        btnBack.setOnClickListener(v -> finish());
        btnAddMore.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));

        loadSellerProducts();
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvSellerProducts);
        btnBack = findViewById(R.id.btnBack);
        btnAddMore = findViewById(R.id.btnAddMore);
    }

    private void setupRecyclerView() {
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(new ArrayList<>(), this);
        rvProducts.setAdapter(adapter);
    }

    private void loadSellerProducts() {
        // Sử dụng tạm getProducts chung, sau có thể dùng API riêng cho Seller
        RetrofitClient.getApiService().getProducts(null, null, null).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ProductAdapter(response.body(), SellerProductsActivity.this);
                    rvProducts.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SellerProductsActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Trong chế độ Seller, click có thể là Edit hoặc Delete
        Toast.makeText(this, "Chạm để xem chi tiết sản phẩm: " + product.getName(), Toast.LENGTH_SHORT).show();
    }
}
