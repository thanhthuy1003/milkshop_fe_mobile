package com.example.milkshop.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherActivity extends AppCompatActivity {

    private EditText edtVoucherCode;
    private Button btnCollect;
    private RecyclerView rvVouchers;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        initViews();
        loadMyVouchers();

        btnCollect.setOnClickListener(v -> collectVoucher());
    }

    private void initViews() {
        edtVoucherCode = findViewById(R.id.edtVoucherCode);
        btnCollect = findViewById(R.id.btnCollectVoucher);
        rvVouchers = findViewById(R.id.rvVouchers);
        tvEmpty = findViewById(R.id.tvEmptyVoucher);

        rvVouchers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMyVouchers() {
        RetrofitClient.getApiService().getMyVouchers().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Logic hiển thị danh sách voucher
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void collectVoucher() {
        String code = edtVoucherCode.getText().toString().trim();
        if (code.isEmpty()) return;

        RetrofitClient.getApiService().collectVoucher(code).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VoucherActivity.this, "Thu thập voucher thành công!", Toast.LENGTH_SHORT).show();
                    loadMyVouchers();
                } else {
                    Toast.makeText(VoucherActivity.this, "Mã voucher không hợp lệ hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VoucherActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
