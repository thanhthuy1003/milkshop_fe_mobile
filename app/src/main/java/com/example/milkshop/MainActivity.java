package com.example.milkshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private RecyclerView rvProducts, rvCategories;
    private ProductAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", true);

        initViews();
        setupRecyclerView();
        setupGuestUI();
        setupBottomNavigation();
        setupSearch();

        imgCart.setOnClickListener(v -> {
            if (isGuest) {
                showLoginRequiredToast();
            } else {
                // Mở Activity giỏ hàng (sẽ triển khai sau)
                Toast.makeText(this, "Mở giỏ hàng...", Toast.LENGTH_SHORT).show();
            }
        });

        btnPromptLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        
        loadProducts(null, null); // Load tất cả sản phẩm lúc khởi đầu
    }

    private void initViews() {
        btnPromptLogin = findViewById(R.id.btnPromptLogin);
        cardGuestPrompt = findViewById(R.id.cardGuestPrompt);
        rvProducts = findViewById(R.id.rvProducts);
        rvCategories = findViewById(R.id.rvCategories);
        imgCart = findViewById(R.id.imgCart);
        edtSearch = findViewById(R.id.edtSearch);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(null, s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
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

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (isGuest) {
                showLoginRequiredToast();
                return false;
            }
            // Logic cho Buyer (Orders, Profile) sẽ được thêm ở đây
            return true;
        });
    }

    private void loadProducts(Integer categoryId, String searchTerm) {
        RetrofitClient.getApiService().getProducts(categoryId, null, searchTerm).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ProductAdapter(response.body(), MainActivity.this);
                    rvProducts.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoginRequiredToast() {
        Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onAddToCartClick(Product product) {
        if (isGuest) {
            showLoginRequiredToast();
        } else {
            // Logic thêm vào giỏ hàng thực tế gọi API sẽ viết ở đây
            Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
