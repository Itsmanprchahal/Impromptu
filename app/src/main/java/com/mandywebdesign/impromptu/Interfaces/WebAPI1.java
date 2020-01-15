package com.mandywebdesign.impromptu.Interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebAPI1 {


        public static String BASE_URL = "https://api.postcodes.io/";
        public static WebAPI1 mInstance;
        Retrofit retrofit;
        public static RegisterApiInterface1 apiInterface;
        private WebAPI1() {

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

            apiInterface = retrofit.create(RegisterApiInterface1.class);
        }


        public static synchronized WebAPI1 getInstance() {
            if (mInstance == null) {
                mInstance = new WebAPI1();
            }
            return mInstance;
        }

        public RegisterApiInterface1 getApi() {
            return retrofit.create(RegisterApiInterface1.class);
        }

}
