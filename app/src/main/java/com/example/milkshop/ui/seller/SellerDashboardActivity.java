package com.example.milkshop.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.OrderStatsResponse;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerDashboardActivity extends AppCompatActivity {

    private CardView cardAddProduct, cardMyProducts, cardOrders, cardRevenue;
    private TextView tvShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        initViews();
        fetchStats();

        cardAddProduct.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
        cardMyProducts.setOnClickListener(v -> startActivity(new Intent(this, SellerProductsActivity.class)));

        cardOrders.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đơn hàng đang được đồng bộ", Toast.LENGTH_SHORT).show();
        });

        cardRevenue.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng doanh thu đang được đồng bộ", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardMyProducts = findViewById(R.id.cardMyProducts);
        cardOrders = findViewById(R.id.cardOrders);
        cardRevenue = findViewById(R.id.cardRevenue);
        tvShopName = findViewById(R.id.tvSellerShopName);
        
        // Giả sử lấy tên shop từ SharedPreferences hoặc User Model
        tvShopName.setText("My Milk Shop");
    }

    private void fetchStats() {
        RetrofitClient.getApiService().getOrderStats(null, null).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        OrderStatsResponse stats = new Gson().fromJson(json, OrderStatsResponse.class);
                        updateStatUI(stats);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SellerDashboardActivity.this, "Lỗi tải thống kê", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatUI(OrderStatsResponse stats) {
        if (stats != null) {
             // Cập nhật thông tin lên Dashboard (ví dụ hiện Toast hoặc log)
             Toast.makeText(this, "Tổng doanh thu: " + stats.getTotalRevenue(), Toast.LENGTH_SHORT).show();
        }
    }
}
