package com.mandywebdesign.impromptu.Interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebAPI {

    public static String BASE_URL = "http://impromptusocial.com/api/";
    public static WebAPI mInstance;
    Retrofit retrofit;
    public static RegisterApiInterface apiInterface;
    private WebAPI() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)

                .build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(RegisterApiInterface.class);
    }


    public static synchronized WebAPI getInstance() {
        if (mInstance == null) {
            mInstance = new WebAPI();
        }
        return mInstance;
    }

    public RegisterApiInterface getApi() {
        return retrofit.create(RegisterApiInterface.class);
    }
}
