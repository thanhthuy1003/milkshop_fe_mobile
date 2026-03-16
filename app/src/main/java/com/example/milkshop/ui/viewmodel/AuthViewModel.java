package com.example.milkshop.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.milkshop.data.model.LoginResponse;
import com.example.milkshop.data.repository.AuthRepository;
import com.example.milkshop.data.repository.Resource;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    private LiveData<Resource<LoginResponse>> loginResult;

    public AuthViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<Resource<LoginResponse>> login(String username, String password) {
        loginResult = authRepository.login(username, password);
        return loginResult;
    }
}
