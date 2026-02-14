package com.example.milkshop.data.api;

import com.example.milkshop.data.model.CartItem;
import com.example.milkshop.data.model.LoginRequest;
import com.example.milkshop.data.model.LoginResponse;
import com.example.milkshop.data.model.Product;
import com.example.milkshop.data.model.RegisterRequest;
import com.example.milkshop.data.model.ResetPasswordRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/authentication/sign-up")
    Call<ResponseBody> signUp(@Body RegisterRequest registerRequest);

    @POST("api/authentication/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @GET("api/products")
    Call<List<Product>> getProducts();

    // --- Cart Endpoints ---
    @GET("api/user/{userId}/cart")
    Call<List<CartItem>> getCart(@Path("userId") String userId);

    @POST("api/user/{userId}/cart")
    Call<ResponseBody> addToCart(@Path("userId") String userId, @Body CartItem item);

    @PATCH("api/user/{userId}/cart/{productId}")
    Call<ResponseBody> updateCartQuantity(@Path("userId") String userId, @Path("productId") int productId, @Body int quantity);

    @DELETE("api/user/{userId}/cart/{productId}")
    Call<ResponseBody> removeFromCart(@Path("userId") String userId, @Path("productId") int productId);
}
