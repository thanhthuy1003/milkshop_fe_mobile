package com.example.milkshop.ui.seller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milkshop.R;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.Product;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtName, edtDesc, edtPrice, edtQty, edtThumb;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initViews();

        btnSubmit.setOnClickListener(v -> handleSubmitProduct());
    }

    private void initViews() {
        edtName = findViewById(R.id.edtAddProductName);
        edtDesc = findViewById(R.id.edtAddProductDesc);
        edtPrice = findViewById(R.id.edtAddProductPrice);
        edtQty = findViewById(R.id.edtAddProductQuantity);
        edtThumb = findViewById(R.id.edtAddProductThumbnail);
        btnSubmit = findViewById(R.id.btnSubmitProduct);
    }

    private void handleSubmitProduct() {
        String name = edtName.getText().toString().trim();
        String desc = edtDesc.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String qtyStr = edtQty.getText().toString().trim();
        String thumb = edtThumb.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập các thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int qty = Integer.parseInt(qtyStr);

        // Tạo đối tượng sản phẩm mới
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(desc);
        newProduct.setSalePrice(price);
        newProduct.setQuantity(qty);
        newProduct.setThumbnail(thumb);
        newProduct.setStatusId(1); // Mặc định là 'Đang bán'

        RetrofitClient.getApiService().createProduct(newProduct).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Đăng sản phẩm thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
