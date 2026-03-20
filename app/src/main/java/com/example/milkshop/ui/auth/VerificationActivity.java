package com.example.milkshop.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private TextInputEditText edtToken;
    private MaterialButton btnVerify;
    private TextView tvResendEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        email = getIntent().getStringExtra("EMAIL");
        
        initViews();
        btnVerify.setOnClickListener(v -> handleVerify());
        tvResendEmail.setOnClickListener(v -> handleResendEmail());
    }

    private void initViews() {
        edtToken = findViewById(R.id.edtToken);
        btnVerify = findViewById(R.id.btnVerify);
        tvResendEmail = findViewById(R.id.tvResendEmail);
    }

    private void handleVerify() {
        String token = edtToken.getText().toString().trim();
        if (token.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().verifyAccount(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerificationActivity.this, "Xác nhận thành công! Bạn có thể đăng nhập.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = "Mã xác nhận không chính xác hoặc đã hết hạn.";
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Log.e("Verification", "Error: " + errorBody.string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(VerificationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(VerificationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResendEmail() {
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin email", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().activateAccount(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerificationActivity.this, "Mã mới đã được gửi đến email của bạn.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerificationActivity.this, "Lỗi khi gửi lại mã.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(VerificationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
