package com.eemeliheinonen.gitcheck;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eemeliheinonen on 28/03/2017.
 */

public class RestClient {

    private static GitHubApiInterface apiService;
    public static final String BASE_URL = "https://api.github.com/";
    public static GitHubApiInterface getClient(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiService = retrofit.create(GitHubApiInterface.class);
        return apiService;
    }
}
