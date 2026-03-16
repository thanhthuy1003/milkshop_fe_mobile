package com.example.milkshop.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.milkshop.data.api.ApiService;
import com.example.milkshop.data.api.RetrofitClient;
import com.example.milkshop.data.model.LoginRequest;
import com.example.milkshop.data.model.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public LiveData<Resource<LoginResponse>> login(String username, String password) {
        MutableLiveData<Resource<LoginResponse>> loginResult = new MutableLiveData<>();
        loginResult.setValue(Resource.loading(null));

        LoginRequest request = new LoginRequest(username, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginResult.setValue(Resource.success(response.body()));
                } else {
                    String errorMsg = "Sai tên đăng nhập hoặc mật khẩu";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loginResult.setValue(Resource.error(errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResult.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return loginResult;
    }
}
