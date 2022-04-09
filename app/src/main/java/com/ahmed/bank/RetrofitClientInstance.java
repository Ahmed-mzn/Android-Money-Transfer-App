package com.ahmed.bank;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static String API_BASE_URL = "https://android-bank-api.herokuapp.com/api/v1/";
    private static Retrofit retrofit;
    private static Gson gson;
    private static OkHttpClient.Builder okHttpClient;
    private static String token;


    public static Retrofit getRetrofitInstance(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("bank_app", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        int cartId = sharedPreferences.getInt("carte_id", 0);
        String code = sharedPreferences.getString("code", "null");
        if(retrofit==null) {

            okHttpClient = new OkHttpClient.Builder();
            okHttpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    Request.Builder newRequest = request.newBuilder().header("Authorization", "Token "+token);

                    return chain.proceed(newRequest.build());
                }
            });

            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
