package com.elegion.tracktor;

import com.elegion.tracktor.api.DistanceMatrixApi;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    public static final Gson GSON = new Gson();
    public static final OkHttpClient OK_HTTP_CLIENT = getClient();
    public static final Retrofit RETROFIT = getRetrofit();
    public static final DistanceMatrixApi MATRIX_API = getMatrixApi();

    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return builder.build();
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static DistanceMatrixApi getMatrixApi() {
        return RETROFIT.create(DistanceMatrixApi.class);
    }
}
