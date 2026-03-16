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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // --- Authentication ---
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/authentication/sign-up")
    Call<ResponseBody> signUp(@Body RegisterRequest registerRequest);

    @POST("api/authentication/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("api/authentication/forgot-password")
    Call<ResponseBody> forgotPassword(@Query("email") String email);

    // --- Product Management (Guest/Buyer/Seller) ---
    @GET("api/products")
    Call<List<Product>> getProducts(
            @Query("categoryId") Integer categoryId,
            @Query("brandId") Integer brandId,
            @Query("searchTerm") String searchTerm
    );

    @POST("api/products")
    Call<ResponseBody> createProduct(@Body Product product);

    @PATCH("api/products/{id}")
    Call<ResponseBody> updateProduct(@Path("id") int id, @Body Product product);

    @DELETE("api/products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    // --- Others (Cart, Checkout, Profile...) ---
    @GET("api/user/{userId}/cart")
    Call<List<CartItem>> getCart(@Path("userId") String userId);
    
    @POST("api/checkout")
    Call<ResponseBody> checkout(@Body Object checkoutRequest);
}
