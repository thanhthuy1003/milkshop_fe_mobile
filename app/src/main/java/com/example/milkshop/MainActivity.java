package com.example.milkshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.Product;
import com.example.milkshop.ui.adapter.ProductAdapter;
import com.example.milkshop.ui.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private boolean isGuest = false;
    private Button btnPromptLogin;
    private ImageView imgCart;
    private CardView cardGuestPrompt;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", false);

        initViews();
        setupRecyclerView();
        setupGuestUI();
        setupBottomNavigation();

        imgCart.setOnClickListener(v -> {
            if (isGuest) {
                showLoginRequiredToast();
            } else {
                Toast.makeText(this, "Mở giỏ hàng...", Toast.LENGTH_SHORT).show();
            }
        });

        btnPromptLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        
        loadProductsFromApi();
    }

    private void initViews() {
        btnPromptLogin = findViewById(R.id.btnPromptLogin);
        cardGuestPrompt = findViewById(R.id.cardGuestPrompt);
        rvProducts = findViewById(R.id.rvProducts);
        imgCart = findViewById(R.id.imgCart);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_orders) {
                if (isGuest) {
                    showLoginRequiredToast();
                    return false;
                }
                Toast.makeText(this, "Đang tải đơn hàng...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_dashboard) {
                // Chỉ Seller/Admin mới xem được phần này, chúng ta sẽ check role sau
                Toast.makeText(this, "Tính năng thống kê dành cho Seller", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_profile) {
                if (isGuest) {
                    showLoginRequiredToast();
                    return false;
                }
                Toast.makeText(this, "Mở trang cá nhân...", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(), this);
        rvProducts.setAdapter(adapter);
    }

    private void setupGuestUI() {
        cardGuestPrompt.setVisibility(isGuest ? View.VISIBLE : View.GONE);
    }

    private void showLoginRequiredToast() {
        Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void loadProductsFromApi() {
        RetrofitClient.getApiService().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ProductAdapter(response.body(), MainActivity.this);
                    rvProducts.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddToCartClick(Product product) {
        if (isGuest) {
            showLoginRequiredToast();
        } else {
            Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
