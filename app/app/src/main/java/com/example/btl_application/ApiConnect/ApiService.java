package com.example.btl_application.ApiConnect;

import com.example.btl_application.Model.AccessResponse;
import com.example.btl_application.Model.DiemHocPhan;
import com.example.btl_application.Model.EducationProgram;
import com.example.btl_application.Model.SimpleMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @POST("account")
    Call<SimpleMessage> createAccount(@Query("account") String account,
                                      @Query("password") String password);
    @PUT("account")
    Call<SimpleMessage> changePassword(@Query("id") String id,
                                       @Query("newPassword") String newPassword);
    @GET("ep")
    Call<List<EducationProgram>> getData(@Query("id") String id);
    @DELETE("ep")
    Call<SimpleMessage> deleteEducationProgram(@Query("id_chuongTrinh") String id);
    @POST("ep")
    Call<SimpleMessage> createNewEducationProgram(@Query("id") String id,
                                    @Query("TenChuongTrinh") String TenChuongTrinh,
                                    @Query("TongSoTinChi") Double TongSoTinChi);
    @GET("ss")
    Call<List<DiemHocPhan>> getDiemHocPhan(@Query("id_chuongTrinh") String id);
    @DELETE("ss")
    Call<SimpleMessage> deleteSectionScore(@Query("id_hocPhan") String id);
    @POST("ss")
    Call<SimpleMessage> createNewSectionScore(@Query("Id_ChuongTrinh") String Id_ChuongTrinh,
                                              @Query("TenMon") String TenMon,
                                              @Query("SoTinChi") Double SoTinChi,
                                              @Query("TiLeTX") String TiLeTX,
                                              @Query("TiLeDiem") String TiLeDiem);
    @PUT("ss")
    Call<SimpleMessage> updateSectionScore(@Query("Id_HocPhan") String Id_HocPhan,
                                           @Query("Tx1") double Tx1,
                                           @Query("Tx2") double Tx2,
                                           @Query("Tx3") double Tx3,
                                           @Query("Tx4") double Tx4,
                                           @Query("Tx5") double Tx5,
                                           @Query("Cc") double Cc,
                                           @Query("Gk") double Gk,
                                           @Query("Ck") double Ck);
}
