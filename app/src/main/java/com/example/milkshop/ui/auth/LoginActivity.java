package com.example.milkshop.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.MainActivity;
import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.LoginRequest;
import com.example.milkshop.data.model.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvRegister, tvContinueAsGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvContinueAsGuest.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("IS_GUEST", true);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvBackToLogin); 
        tvContinueAsGuest = findViewById(R.id.tvContinueAsGuest);
    }

    private void handleLogin() {
        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(user, pass);
        
        // Gọi API Login thực tế
        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("IS_GUEST", false);
                    startActivity(intent);
                    finish();
                } else {
                    String error = "Sai tên đăng nhập hoặc mật khẩu";
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("LoginActivity", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Login API Failed", t);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
