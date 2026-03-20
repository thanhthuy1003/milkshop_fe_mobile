package com.example.milkshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.Banner;
import com.example.milkshop.data.model.Category;
import com.example.milkshop.data.model.Product;
import com.example.milkshop.ui.adapter.BannerAdapter;
import com.example.milkshop.ui.adapter.CategoryAdapter;
import com.example.milkshop.ui.adapter.ProductAdapter;
import com.example.milkshop.ui.auth.LoginActivity;
import com.example.milkshop.ui.seller.SellerDashboardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        ProductAdapter.OnProductClickListener,
        CategoryAdapter.OnCategoryClickListener {

    private static final String TAG = "MainActivity";
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
        if (userRole == null)
            userRole = "";

        initViews();
        setupRecyclerViews();
        setupGuestUI();
        setupBottomNavigation();
        setupSearch();
        setupHeader();

        btnPromptLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnGoToSellerDashboard
                .setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SellerDashboardActivity.class)));

        loadProducts(null, null);
        loadCategories();
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

    private void showLoginRequiredToast() {
        Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerViews() {
        // Categories
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Products
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(new ArrayList<>(), this);
        rvProducts.setAdapter(productAdapter);
        rvProducts.setNestedScrollingEnabled(false);
    }

    private void loadCategories() {
        RetrofitClient.getApiService(this).getCategories("Active", null, 1, 50).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        List<Category> categories = new ArrayList<>();
                        // Add "All" category at the beginning
                        categories.add(new Category(0, "Tất cả", 0));

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(json, JsonElement.class);
                        JsonArray array = null;

                        if (element.isJsonArray()) {
                            array = element.getAsJsonArray();
                        } else if (element.isJsonObject()) {
                            JsonObject obj = element.getAsJsonObject();
                            if (obj.has("items")) array = obj.getAsJsonArray("items");
                            else if (obj.has("data")) array = obj.getAsJsonArray("data");
                        }

                        if (array != null) {
                            List<Category> apiCategories = gson.fromJson(array, new TypeToken<List<Category>>() {}.getType());
                            categories.addAll(apiCategories);
                        }

                        categoryAdapter = new CategoryAdapter(categories, MainActivity.this);
                        rvCategories.setAdapter(categoryAdapter);
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi parse categories", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Lỗi load categories", t);
            }
        });
    }

    private void loadBanners() {
        RetrofitClient.getApiService(this).getBanners(1, 10).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
                        List<Banner> banners = new ArrayList<>();

                        if (jsonElement.isJsonArray()) {
                            banners = gson.fromJson(jsonElement, new TypeToken<List<Banner>>() {
                            }.getType());
                        } else if (jsonElement.isJsonObject()) {
                            JsonObject obj = jsonElement.getAsJsonObject();
                            JsonArray array = obj.has("items") ? obj.getAsJsonArray("items")
                                    : obj.getAsJsonArray("data");
                            if (array != null) {
                                banners = gson.fromJson(array, new TypeToken<List<Banner>>() {
                                }.getType());
                            }
                        }

                        if (!banners.isEmpty()) {
                            bannerAdapter = new BannerAdapter(banners);
                            viewPagerBanners.setAdapter(bannerAdapter);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi load banners", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Lỗi connect banners", t);
            }
        });
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadProducts(null, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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
            if (id == R.id.nav_home)
                return true;
            if (id == R.id.nav_chat) {
                startActivity(new Intent(this, com.example.milkshop.ui.profile.ConversationListActivity.class));
                return true;
            }
            if (id == R.id.nav_profile) {
                if (isGuest) {
                    showLoginRequiredToast();
                } else {
                    startActivity(new Intent(this, com.example.milkshop.ui.profile.ProfileActivity.class));
                }
                return true;
            }
            return true;
        });
    }

    private void loadProducts(Integer categoryId, String searchTerm) {
        RetrofitClient.getApiService(this).getProducts(categoryId, null, searchTerm, null, null, "Active", null, 1, 50)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String json = response.body().string();
                                List<Product> products = new ArrayList<>();
                                Gson gson = new Gson();
                                JsonElement element = gson.fromJson(json, JsonElement.class);

                                if (element.isJsonArray()) {
                                    products = gson.fromJson(element, new TypeToken<List<Product>>() {
                                    }.getType());
                                } else if (element.isJsonObject()) {
                                    JsonObject obj = element.getAsJsonObject();
                                    JsonArray array = null;

                                    if (obj.has("items"))
                                        array = obj.getAsJsonArray("items");
                                    else if (obj.has("data"))
                                        array = obj.getAsJsonArray("data");
                                    else if (obj.has("products"))
                                        array = obj.getAsJsonArray("products");
                                    else if (obj.has("results"))
                                        array = obj.getAsJsonArray("results");
                                    else if (obj.has("content"))
                                        array = obj.getAsJsonArray("content");

                                    if (array != null) {
                                        products = gson.fromJson(array, new TypeToken<List<Product>>() {
                                        }.getType());
                                    } else {
                                        Log.w(TAG, "Không tìm thấy mảng sản phẩm trong JSON. Các keys hiện có: "
                                                + obj.keySet());
                                    }
                                }

                                if (!products.isEmpty()) {
                                    productAdapter = new ProductAdapter(products, MainActivity.this);
                                    rvProducts.setAdapter(productAdapter);
                                } else {
                                    Log.d(TAG, "Danh sách sản phẩm trống.");
                                    Toast.makeText(MainActivity.this, "Không có sản phẩm nào để hiển thị",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Lỗi load products", e);
                            }
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
