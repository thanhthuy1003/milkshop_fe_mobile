package com.example.milkshop.ui.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.CheckoutRequest;
import com.example.milkshop.data.model.Province;
import com.example.milkshop.data.model.District;
import com.example.milkshop.data.model.Ward;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtAddress;
    private Spinner spnProvince, spnDistrict, spnWard;
    private RadioGroup rgPayment;
    private Button btnConfirm;
    private TextView tvShippingFee, tvTotalCheckout;

    private List<Province> provinceList = new ArrayList<>();
    private List<District> districtList = new ArrayList<>();
    private List<Ward> wardList = new ArrayList<>();
    private double totalProductPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalProductPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0);

        initViews();
        loadProvinces();

        btnConfirm.setOnClickListener(v -> handleCheckout());

        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDistricts(provinceList.get(position).getProvinceId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadWards(districtList.get(position).getDistrictId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateShippingFee();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        tvTotalCheckout.setText(String.format("%,.0fđ", totalProductPrice));
    }

    private void initViews() {
        edtName = findViewById(R.id.edtReceiverName);
        edtPhone = findViewById(R.id.edtReceiverPhone);
        edtAddress = findViewById(R.id.edtReceiverAddress);
        spnProvince = findViewById(R.id.spnProvince);
        spnDistrict = findViewById(R.id.spnDistrict);
        spnWard = findViewById(R.id.spnWard);
        rgPayment = findViewById(R.id.rgPaymentMethod);
        btnConfirm = findViewById(R.id.btnConfirmCheckout);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalCheckout = findViewById(R.id.tvTotalCheckout);
    }

    private void loadProvinces() {
        RetrofitClient.getApiService().getProvinces().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        provinceList = new Gson().fromJson(json, new TypeToken<List<Province>>(){}.getType());
                        ArrayAdapter<Province> adapter = new ArrayAdapter<>(CheckoutActivity.this, android.R.layout.simple_spinner_item, provinceList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnProvince.setAdapter(adapter);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void loadDistricts(int provinceId) {
        RetrofitClient.getApiService().getDistricts(provinceId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        districtList = new Gson().fromJson(json, new TypeToken<List<District>>(){}.getType());
                        ArrayAdapter<District> adapter = new ArrayAdapter<>(CheckoutActivity.this, android.R.layout.simple_spinner_item, districtList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnDistrict.setAdapter(adapter);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void loadWards(int districtId) {
        RetrofitClient.getApiService().getWards(districtId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        wardList = new Gson().fromJson(json, new TypeToken<List<Ward>>(){}.getType());
                        ArrayAdapter<Ward> adapter = new ArrayAdapter<>(CheckoutActivity.this, android.R.layout.simple_spinner_item, wardList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWard.setAdapter(adapter);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void calculateShippingFee() {
        if (districtList.isEmpty() || wardList.isEmpty()) return;
        
        int districtId = districtList.get(spnDistrict.getSelectedItemPosition()).getDistrictId();
        String wardCode = wardList.get(spnWard.getSelectedItemPosition()).getWardCode();
        
        RetrofitClient.getApiService().getShippingFee(districtId, wardCode, 1000).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        double fee = Double.parseDouble(json);
                        tvShippingFee.setText(String.format("%,.0fđ", fee));
                        updateTotalCost(fee);
                    } catch (Exception e) { 
                        tvShippingFee.setText("Lỗi tính phí");
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void updateTotalCost(double shippingFee) {
        double total = totalProductPrice + shippingFee;
        tvTotalCheckout.setText(String.format("%,.0fđ", total));
    }

    private void handleCheckout() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang xử lý đơn hàng...", Toast.LENGTH_SHORT).show();
    }
}
