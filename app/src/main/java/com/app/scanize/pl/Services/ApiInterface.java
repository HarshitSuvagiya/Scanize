package com.app.scanize.pl.Services;

import com.app.scanize.pl.Model.CountryCodeModel;
import com.app.scanize.pl.Model.SendOtpModel;
import com.app.scanize.pl.Model.SubStatusModel;
import com.app.scanize.pl.Utils.Constans;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(Constans.CODEKEYURL)
    Call<CountryCodeModel> getCountryCode();


    @GET("app_apis/red1.php?c=palestine&s=drawit")
    Call<String> getKey(@Query("tid")String key);

    @POST(Constans.KEYURL)
    Call<SubStatusModel> getStatus(@Body RequestBody rawObj);

    @POST(Constans.KEYURL)
    Call<SendOtpModel> sendAPI1Otp(@Body RequestBody rawObj);

    @POST(Constans.KEYURL)
    Call<SendOtpModel> verifyOtp(@Body RequestBody rawObj);



}
