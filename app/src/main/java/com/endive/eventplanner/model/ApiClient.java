package com.endive.eventplanner.model;

import com.endive.eventplanner.util.EventConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = EventConstant.BASE_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(ApiClient.okClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient okClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        BASE_URL = newApiBaseUrl;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .baseUrl(BASE_URL).build();
    }
}
