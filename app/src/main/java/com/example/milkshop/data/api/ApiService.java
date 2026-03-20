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
    Call<com.example.milkshop.data.model.UserProfile> getProfile();

    @PATCH("api/user/account/profile")
    Call<ResponseBody> updateProfile(@Body Object profileUpdate);

    @PATCH("api/user/account/change-password")
    Call<ResponseBody> changePassword(@Body Object passwordChange);

    // --- CartController ---
    @GET("api/user/{userId}/cart")
    Call<ResponseBody> getCart(@Path("userId") String userId, @Query("Page") Integer page, @Query("PageSize") Integer pageSize);

    @POST("api/user/{userId}/cart")
    Call<ResponseBody> addToCart(@Path("userId") String userId, @Body Object addToCartModel);

    @PATCH("api/user/{userId}/cart/{productId}")
    Call<ResponseBody> updateCartItem(@Path("userId") String userId, @Path("productId") String productId, @Body Object updateCartItemModel);

    @DELETE("api/user/{userId}/cart/{productId}")
    Call<ResponseBody> deleteCartItem(@Path("userId") String userId, @Path("productId") String productId);

    @DELETE("api/user/{userId}/cart")
    Call<ResponseBody> clearCart(@Path("userId") String userId);

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
    Call<ResponseBody> getProducts(@Query("categoryId") Integer categoryId, @Query("brandId") Integer brandId, @Query("searchTerm") String searchTerm);

    @POST("api/products")
    Call<ResponseBody> createProduct(@Body Product product);

    @PATCH("api/products/{productId}/preorder")
    Call<ResponseBody> updatePreorder(@Path("productId") String productId, @Body Object preorderModel);

    // --- Dashboard (Seller/Admin) ---
    @GET("api/dashboard/orders")
    Call<ResponseBody> getDashboardOrders(@Query("Page") Integer page, @Query("PageSize") Integer pageSize, @Query("OrderStatus") String status);

    @GET("api/dashboard/orders/stats")
    Call<ResponseBody> getOrderStats(@Query("FromOrderDate") String from, @Query("ToOrderDate") String to);

    @GET("api/dashboard/products/stats")
    Call<ResponseBody> getProductStats(@Query("From") String from, @Query("To") String to);

    @PATCH("api/dashboard/orders/{id}/status")
    Call<ResponseBody> updateOrderStatus(@Path("id") String id, @Body Object statusModel);

    // --- Shipping (GHN) ---
    @GET("api/shipping/provinces")
    Call<ResponseBody> getProvinces();

    @GET("api/shipping/districts/{provinceId}")
    Call<ResponseBody> getDistricts(@Path("provinceId") int provinceId);

    @GET("api/shipping/wards/{districtId}")
    Call<ResponseBody> getWards(@Path("districtId") int districtId);

    @GET("api/shipping/fee")
    Call<ResponseBody> getShippingFee(@Query("FromDistrictId") int fromDistrictId, @Query("FromWardCode") String fromWardCode, @Query("TotalWeight") int totalWeight);

    // --- Checkout ---
    @POST("api/checkout")
    Call<ResponseBody> checkout(@Body CheckoutRequest checkoutRequest);

    @POST("api/checkout/preorder")
    Call<ResponseBody> checkoutPreorder(@Body Object preorderCheckoutRequest);
    // --- BannerController ---
    @GET("api/banners")
    Call<ResponseBody> getBanners(@Query("Page") Integer page, @Query("PageSize") Integer pageSize);

    // --- ConversationController ---
    @GET("api/conversations")
    Call<ResponseBody> getConversations(@Query("Page") Integer page, @Query("PageSize") Integer pageSize);

    @GET("api/conversations/{conversationId}/messages")
    Call<ResponseBody> getMessages(@Path("conversationId") String id, @Query("Page") Integer page, @Query("PageSize") Integer pageSize);

    @POST("api/conversations/{otherUserId}")
    Call<ResponseBody> startConversation(@Path("otherUserId") String otherUserId, @Body Object sendMessageModel);

    // --- ReviewController ---
    @GET("api/products/{productId}/reviews")
    Call<ResponseBody> getProductReviews(@Path("productId") String productId, @Query("Page") Integer page, @Query("PageSize") Integer pageSize);

    @POST("api/reviews")
    Call<ResponseBody> createReview(@Body Object createReviewModel);
}
