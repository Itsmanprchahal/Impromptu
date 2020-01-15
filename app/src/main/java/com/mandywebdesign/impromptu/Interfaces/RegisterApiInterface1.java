package com.mandywebdesign.impromptu.Interfaces;

import com.mandywebdesign.impromptu.Retrofit.PostCode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RegisterApiInterface1 {
    @GET("postcodes/{input}")
    Call<PostCode> postcode(
            @Path("input") String postcode
    );

}
