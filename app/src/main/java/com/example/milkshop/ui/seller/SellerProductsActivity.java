package com.example.milkshop.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private static final String TAG = "SellerProductsActivity";
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
        RetrofitClient.getApiService(this).getProducts(null, null, null, null, null, "All", null, 1, 50).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Log.d(TAG, "API Response: " + jsonString);

                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
                        List<Product> products = new ArrayList<>();

                        if (jsonElement.isJsonArray()) {
                            Type listType = new TypeToken<List<Product>>() {
                            }.getType();
                            products = gson.fromJson(jsonElement, listType);
                        } else if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            JsonArray itemsArray = null;

                            if (jsonObject.has("items"))
                                itemsArray = jsonObject.getAsJsonArray("items");
                            else if (jsonObject.has("data"))
                                itemsArray = jsonObject.getAsJsonArray("data");
                            else if (jsonObject.has("products"))
                                itemsArray = jsonObject.getAsJsonArray("products");
                            else if (jsonObject.has("results"))
                                itemsArray = jsonObject.getAsJsonArray("results");
                            else if (jsonObject.has("content"))
                                itemsArray = jsonObject.getAsJsonArray("content");

                            if (itemsArray != null) {
                                Type listType = new TypeToken<List<Product>>() {
                                }.getType();
                                products = gson.fromJson(itemsArray, listType);
                            } else {
                                Log.w(TAG, "Không tìm thấy mảng sản phẩm trong JSON. Các keys hiện có: "
                                        + jsonObject.keySet());
                            }
                        }

                        if (!products.isEmpty()) {
                            adapter = new ProductAdapter(products, SellerProductsActivity.this);
                            rvProducts.setAdapter(adapter);
                        } else {
                            Toast.makeText(SellerProductsActivity.this, "Không tìm thấy sản phẩm nào",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Lỗi đọc response", e);
                        Toast.makeText(SellerProductsActivity.this, "Lỗi đọc dữ liệu: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SellerProductsActivity.this, "Lỗi tải sản phẩm: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network Error", t);
                Toast.makeText(SellerProductsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onAddToCartClick(Product product) {
        Toast.makeText(this, "Xem chi tiết: " + product.getName(), Toast.LENGTH_SHORT).show();
    }
}
