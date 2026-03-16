package com.example.milkshop.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.local.TokenManager;
import com.example.milkshop.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private MaterialButton btnLogout, btnMyVouchers;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tokenManager = new TokenManager(this);
        initViews();
        loadUserProfile();

        btnLogout.setOnClickListener(v -> handleLogout());
        
        btnMyVouchers.setOnClickListener(v -> {
            // Chuyển sang màn hình Ví Voucher (sẽ triển khai sau)
            Toast.makeText(this, "Đang mở Ví Voucher...", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyVouchers = findViewById(R.id.btnMyVouchers);
    }

    private void loadUserProfile() {
        RetrofitClient.getApiService().getProfile().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Trong thực tế, bạn nên parse JSON từ response.body()
                    Toast.makeText(ProfileActivity.this, "Đã tải thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi tải hồ sơ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogout() {
        tokenManager.clear();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
