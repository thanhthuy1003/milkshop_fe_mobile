package com.example.milkshop.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.local.TokenManager;
import com.example.milkshop.data.model.UserProfile;
import com.example.milkshop.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvRole, tvUsernameVal, tvEmailVal, tvPhoneVal;
    private MaterialButton btnLogout, btnMyVouchers;
    private TokenManager tokenManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tokenManager = new TokenManager(this);
        initViews();
        setupToolbar();
        loadUserProfile();

        btnLogout.setOnClickListener(v -> handleLogout());
        btnMyVouchers.setOnClickListener(v -> {
            startActivity(new Intent(this, VoucherActivity.class));
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvName = findViewById(R.id.tvProfileName);
        tvRole = findViewById(R.id.tvProfileRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyVouchers = findViewById(R.id.btnMyVouchers);

        // Info items
        View infoUsername = findViewById(R.id.infoUsername);
        ((TextView)infoUsername.findViewById(R.id.tvLabel)).setText("Username");
        tvUsernameVal = infoUsername.findViewById(R.id.tvValue);

        View infoEmail = findViewById(R.id.infoEmail);
        ((TextView)infoEmail.findViewById(R.id.tvLabel)).setText("Email");
        tvEmailVal = infoEmail.findViewById(R.id.tvValue);

        View infoPhone = findViewById(R.id.infoPhone);
        ((TextView)infoPhone.findViewById(R.id.tvLabel)).setText("Số điện thoại");
        tvPhoneVal = infoPhone.findViewById(R.id.tvValue);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadUserProfile() {
        RetrofitClient.getApiService(this).getProfile().enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body();
                    displayProfile(profile);
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải hồ sơ: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(UserProfile profile) {
        tvName.setText(profile.getFullName() != null && !profile.getFullName().trim().isEmpty() ? 
            profile.getFullName() : profile.getUsername());
        tvRole.setText(profile.getRole());
        tvUsernameVal.setText(profile.getUsername());
        tvEmailVal.setText(profile.getEmail());
        tvPhoneVal.setText(profile.getPhoneNumber());
    }

    private void handleLogout() {
        tokenManager.clear();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
