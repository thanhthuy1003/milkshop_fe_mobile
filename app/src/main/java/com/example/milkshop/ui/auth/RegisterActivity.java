package com.example.milkshop.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.RegisterRequest;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtFirstName, edtLastName, edtEmail, edtPhone, edtPassword, edtConfirmPassword, edtShopName;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private String currentRole = "Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        currentRole = getIntent().getStringExtra("ROLE");
        if (currentRole == null) currentRole = "Customer";

        initViews();
        setupRoleUI();
        btnRegister.setOnClickListener(v -> handleRegister());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtShopName = findViewById(R.id.edtShopName);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void setupRoleUI() {
        if ("Seller".equals(currentRole)) {
            edtShopName.setVisibility(android.view.View.VISIBLE);
        } else {
            edtShopName.setVisibility(android.view.View.GONE);
        }
    }

    private void handleRegister() {
        String username = edtUsername.getText().toString().trim();
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String shopName = edtShopName.getText().toString().trim();
        
        String role = currentRole;

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                username,
                firstName,
                lastName,
                phone,
                email,
                password,
                confirmPassword,
                role
        );

        Log.d("RegisterActivity", "Request Payload: " + new Gson().toJson(request));

        RetrofitClient.getApiService().signUp(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng kiểm tra email.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                    finish();
                } else {
                    StringBuilder errorMessage = new StringBuilder("Đăng ký thất bại (Code: " + response.code() + ")");
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errorJson = errorBody.string();
                            Log.e("RegisterActivity", "Error Response: " + errorJson);
                            errorMessage.append("\n").append(errorJson);
                        }
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "Error parsing error body", e);
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("RegisterActivity", "API Call Failed", t);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
