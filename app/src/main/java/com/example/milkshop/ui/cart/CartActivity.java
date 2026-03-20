package com.example.milkshop.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.CartItem;
import com.example.milkshop.data.model.UpdateCartItemModel;
import com.example.milkshop.ui.adapter.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice, tvEmptyCart;
    private Button btnCheckout;
    private ImageView btnBack;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        loadCartItems();

        btnBack.setOnClickListener(v -> finish());
        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, com.example.milkshop.ui.checkout.CheckoutActivity.class);
            intent.putExtra("TOTAL_PRICE", calculateCartTotal());
            startActivity(intent);
        });
    }

    private void initViews() {
        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, this);
        rvCartItems.setAdapter(adapter);
    }

    private void loadCartItems() {
        // userId should be retrieved from session
        String userId = "00000000-0000-0000-0000-000000000000"; 
        
        RetrofitClient.getApiService().getCart(userId, 1, 50).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        List<CartItem> items = new Gson().fromJson(json, new TypeToken<List<CartItem>>(){}.getType());
                        if (items != null) {
                            cartItems.clear();
                            cartItems.addAll(items);
                            adapter.notifyDataSetChanged();
                            updateTotalPriceDisplay();
                            tvEmptyCart.setVisibility(cartItems.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                } else {
                    tvEmptyCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối giỏ hàng", Toast.LENGTH_SHORT).show();
                tvEmptyCart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateTotalPriceDisplay() {
        tvTotalPrice.setText(String.format("%,.0fđ", calculateCartTotal()));
    }

    private double calculateCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    @Override
    public void onQuantityChange(CartItem item, int newQuantity) {
        String userId = "00000000-0000-0000-0000-000000000000";
        UpdateCartItemModel model = new UpdateCartItemModel(newQuantity);
        
        RetrofitClient.getApiService().updateCartItem(userId, item.getProductId(), model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    item.setQuantity(newQuantity);
                    adapter.notifyDataSetChanged();
                    updateTotalPriceDisplay();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    @Override
    public void onRemoveItem(CartItem item) {
        String userId = "00000000-0000-0000-0000-000000000000";
        RetrofitClient.getApiService().deleteCartItem(userId, item.getProductId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    cartItems.remove(item);
                    adapter.notifyDataSetChanged();
                    updateTotalPriceDisplay();
                    if (cartItems.isEmpty()) tvEmptyCart.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
}
