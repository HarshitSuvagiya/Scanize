package com.girls.scanize.selfiecamera.Services;

import com.girls.scanize.selfiecamera.Model.CountryCodeModel;
import com.girls.scanize.selfiecamera.Model.SendOtpModel;
import com.girls.scanize.selfiecamera.Model.SubStatusModel;
import com.girls.scanize.selfiecamera.Model.sendOTP;
import com.girls.scanize.selfiecamera.Utils.Constans;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
