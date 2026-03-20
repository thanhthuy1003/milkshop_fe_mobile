package com.example.milkshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.milkshop.ui.adapter.BannerAdapter;
import com.example.milkshop.data.model.Banner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.ResponseBody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.ui.seller.SellerDashboardActivity;
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
    private CardView cardSellerPrompt;
    private Button btnGoToSellerDashboard;
    private ViewPager2 viewPagerBanners;
    private BannerAdapter bannerAdapter;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", true);
        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) userRole = "";

        initViews();
        setupRecyclerView();
        setupGuestUI();
        setupBottomNavigation();
        setupSearch();

        imgCart.setOnClickListener(v -> {
            if (isGuest) {
                showLoginRequiredToast();
            } else {
                startActivity(new Intent(MainActivity.this, com.example.milkshop.ui.cart.CartActivity.class));
            }
        });

        btnPromptLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        
        btnGoToSellerDashboard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SellerDashboardActivity.class)));

        loadProducts(null, null);
        loadBanners();
    }

    private void initViews() {
        btnPromptLogin = findViewById(R.id.btnPromptLogin);
        cardGuestPrompt = findViewById(R.id.cardGuestPrompt);
        rvProducts = findViewById(R.id.rvProducts);
        rvCategories = findViewById(R.id.rvCategories);
        imgCart = findViewById(R.id.imgCart);
        edtSearch = findViewById(R.id.edtSearch);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        cardSellerPrompt = findViewById(R.id.cardSellerPrompt);
        btnGoToSellerDashboard = findViewById(R.id.btnGoToSellerDashboard);
        viewPagerBanners = findViewById(R.id.viewPagerBanners);
    }

    private void loadBanners() {
        RetrofitClient.getApiService().getBanners(1, 10).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        List<Banner> banners = new Gson().fromJson(json, new TypeToken<List<Banner>>(){}.getType());
                        if (banners != null && !banners.isEmpty()) {
                            bannerAdapter = new BannerAdapter(banners);
                            viewPagerBanners.setAdapter(bannerAdapter);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
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
        
        if (!isGuest && "Seller".equalsIgnoreCase(userRole)) {
            cardSellerPrompt.setVisibility(View.VISIBLE);
        } else {
            cardSellerPrompt.setVisibility(View.GONE);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_chat) {
                startActivity(new Intent(this, com.example.milkshop.ui.profile.ConversationListActivity.class));
                return true;
            }
            if (id == R.id.nav_profile) {
                // Logged in profile view
                Toast.makeText(this, "Tính năng Cá nhân", Toast.LENGTH_SHORT).show();
                return true;
            }
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
