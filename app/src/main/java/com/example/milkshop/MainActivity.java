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
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;
import com.example.milkshop.ui.adapter.BannerAdapter;
import com.example.milkshop.ui.adapter.CategoryAdapter;
import com.example.milkshop.data.model.Banner;
import com.example.milkshop.data.model.Category;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.ResponseBody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class MainActivity extends AppCompatActivity implements 
    ProductAdapter.OnProductClickListener, 
    CategoryAdapter.OnCategoryClickListener {

    private boolean isGuest = false;
    private Button btnPromptLogin;
    private CardView cardGuestPrompt;
    private RecyclerView rvProducts, rvCategories;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private BottomNavigationView bottomNavigationView;
    private EditText edtSearch;
    private CardView cardSellerPrompt;
    private Button btnGoToSellerDashboard;
    private ViewPager2 viewPagerBanners;
    private BannerAdapter bannerAdapter;
    private TextView tvWelcome, tvUserName;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", true);
        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) userRole = "";

        initViews();
        setupRecyclerViews();
        setupGuestUI();
        setupBottomNavigation();
        setupSearch();
        setupHeader();

        btnPromptLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnGoToSellerDashboard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SellerDashboardActivity.class)));

        loadProducts(null, null);
        loadBanners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserName = findViewById(R.id.tvUserName);
        btnPromptLogin = findViewById(R.id.btnPromptLogin);
        cardGuestPrompt = findViewById(R.id.cardGuestPrompt);
        rvProducts = findViewById(R.id.rvProducts);
        rvCategories = findViewById(R.id.rvCategories);
        edtSearch = findViewById(R.id.edtSearch);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        cardSellerPrompt = findViewById(R.id.cardSellerPrompt);
        btnGoToSellerDashboard = findViewById(R.id.btnGoToSellerDashboard);
        viewPagerBanners = findViewById(R.id.viewPagerBanners);
    }

    private void setupHeader() {
        if (isGuest) {
            tvWelcome.setText("Chào mừng đến với MilkShop");
            tvUserName.setText("Khách hàng");
        } else {
            tvWelcome.setText("Rất vui được gặp lại,");
            tvUserName.setText(userRole.equalsIgnoreCase("Seller") ? "Người bán" : "Người mua");
        }
    }

    private void setupRecyclerViews() {
        // Categories
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(0, "Tất cả", 0));
        categories.add(new Category(1, "Sữa bột", 0));
        categories.add(new Category(2, "Sữa tươi", 0));
        categories.add(new Category(3, "Trà sữa", 0));
        categories.add(new Category(4, "Phụ kiện", 0));
        
        categoryAdapter = new CategoryAdapter(categories, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Products
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(new ArrayList<>(), this);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setNestedScrollingEnabled(false);
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
                Toast.makeText(this, "Tính năng Cá nhân", Toast.LENGTH_SHORT).show();
                return true;
            }
            return true;
        });
    }

    private void loadProducts(Integer categoryId, String searchTerm) {
        RetrofitClient.getApiService().getProducts(categoryId, null, searchTerm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        List<Product> products = null;
                        Gson gson = new Gson();
                        
                        if (json.trim().startsWith("{")) {
                            JsonObject obj = gson.fromJson(json, JsonObject.class);
                            JsonElement itemsElement = obj.get("items");
                            if (itemsElement == null) itemsElement = obj.get("data");
                            if (itemsElement != null && itemsElement.isJsonArray()) {
                                products = gson.fromJson(itemsElement, new TypeToken<List<Product>>(){}.getType());
                            }
                        } else if (json.trim().startsWith("[")) {
                            products = gson.fromJson(json, new TypeToken<List<Product>>(){}.getType());
                        }

                        if (products != null) {
                            productAdapter = new ProductAdapter(products, MainActivity.this);
                            rvProducts.setAdapter(productAdapter);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Integer catId = category.getId() == 0 ? null : category.getId();
        loadProducts(catId, null);
    }

    @Override
    public void onAddToCartClick(Product product) {
        if (isGuest) {
            Toast.makeText(this, "Vui lòng đăng nhập để mua hàng!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
