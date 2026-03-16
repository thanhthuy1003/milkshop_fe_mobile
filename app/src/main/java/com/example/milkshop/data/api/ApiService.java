package com.example.milkshop.data.api;

import com.example.milkshop.data.model.CartItem;
import com.example.milkshop.data.model.CheckoutRequest;
import com.example.milkshop.data.model.LoginRequest;
import com.example.milkshop.data.model.LoginResponse;
import com.example.milkshop.data.model.Product;
import com.example.milkshop.data.model.RegisterRequest;
import com.example.milkshop.data.model.ResetPasswordRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // --- AuthenticationController ---
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/authentication/sign-up")
    Call<ResponseBody> signUp(@Body RegisterRequest registerRequest);

    @POST("api/authentication/forgot-password")
    Call<ResponseBody> forgotPassword(@Query("email") String email);

    @POST("api/authentication/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("api/authentication/refresh-token")
    Call<LoginResponse> refreshToken(@Query("token") String token);

    // --- UserController ---
    @GET("api/user/account/profile")
    Call<ResponseBody> getProfile();

    @PATCH("api/user/account/profile")
    Call<ResponseBody> updateProfile(@Body Object profileUpdate);

    @PATCH("api/user/account/change-password")
    Call<ResponseBody> changePassword(@Body Object passwordChange);

    // --- ImageController ---
    @Multipart
    @POST("api/image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    // --- VoucherController ---
    @GET("api/vouchers")
    Call<ResponseBody> getMyVouchers();

    @POST("api/vouchers/code/{code}")
    Call<ResponseBody> collectVoucher(@Path("code") String code);

    // --- Product Management ---
    @GET("api/products")
    Call<List<Product>> getProducts(@Query("categoryId") Integer categoryId, @Query("brandId") Integer brandId, @Query("searchTerm") String searchTerm);

    @POST("api/products")
    Call<ResponseBody> createProduct(@Body Product product);

    // --- Checkout ---
    @POST("api/checkout")
    Call<ResponseBody> checkout(@Body CheckoutRequest checkoutRequest);
}
