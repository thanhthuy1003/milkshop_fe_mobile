package com.example.milkshop.data.api;

import android.content.Context;
import com.example.milkshop.data.local.TokenManager;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Local testing: http://10.0.2.2:5000/ (Emulator) or http://<your-ip>:5000/ (Physical device)
    private static final String BASE_URL = "http://192.168.87.177:5000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        return getApiService(null);
    }

    public static ApiService getApiService(Context context) {
        if (retrofit == null || context != null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            // Add Auth Interceptor if context is provided
            if (context != null) {
                final TokenManager tokenManager = new TokenManager(context);
                clientBuilder.addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = tokenManager.getToken();

                    if (token != null && !token.isEmpty()) {
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                    return chain.proceed(original);
                });
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
