package com.example.milkshop.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.milkshop.R;

public class PreRegisterActivity extends AppCompatActivity {

    private CardView cardBuyer, cardSeller;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_register);

        initViews();

        cardBuyer.setOnClickListener(v -> startRegister("Customer"));
        cardSeller.setOnClickListener(v -> startRegister("Seller"));
        
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        cardBuyer = findViewById(R.id.cardBuyer);
        cardSeller = findViewById(R.id.cardSeller);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void startRegister(String role) {
        Intent intent = new Intent(PreRegisterActivity.this, RegisterActivity.class);
        intent.putExtra("ROLE", role);
        startActivity(intent);
    }
}
