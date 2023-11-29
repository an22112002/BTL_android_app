package com.example.btl_application.ApiConnect;

import com.example.btl_application.Model.AccessResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    final String BASEURL = "http://192.168.1.103:5000/";
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    @GET("account")
    Call<AccessResponse> getAccess(@Query("account") String account,
                                   @Query("password") String password);
}
