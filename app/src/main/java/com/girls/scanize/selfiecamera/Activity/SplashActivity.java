package com.girls.scanize.selfiecamera.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.girls.scanize.selfiecamera.Intro.WelcomeActivity;
import com.girls.scanize.selfiecamera.Model.CountryCodeModel;
import com.girls.scanize.selfiecamera.Model.SubStatusModel;
import com.girls.scanize.selfiecamera.R;
import com.girls.scanize.selfiecamera.Services.ApiInterface;
import com.girls.scanize.selfiecamera.Services.RetrofitClient;
import com.girls.scanize.selfiecamera.Utils.Constans;
import com.girls.scanize.selfiecamera.Utils.PrefrenceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "data-->";
    ProgressBar progressBar;
    PrefrenceHandler prefrenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefrenceHandler = new PrefrenceHandler(SplashActivity.this);
        progressBar = findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Constans.isOnline(SplashActivity.this)) {
                    GetCountryCode();
                } else {
                    Constans.NoInternetDialog(SplashActivity.this);
                }
            }
        }, 1000);
    }

    public void GetCountryCode() {
        progressBar.setVisibility(View.VISIBLE);
        final ApiInterface apiInterface = RetrofitClient.getClient1().create(ApiInterface.class);
        apiInterface.getCountryCode().enqueue(new Callback<CountryCodeModel>() {
            @Override
            public void onResponse(Call<CountryCodeModel> call, Response<CountryCodeModel> response) {

                CountryCodeModel adsDataModel = response.body();
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    if (adsDataModel.getCode().equals("PS")) {
                        if (prefrenceHandler.getBoolean(Constans.IS_FIRST_TIME_LAUNCH)) {
                            if (prefrenceHandler.getBoolean(Constans.IsLogin)) {
                                if (Constans.isOnline(SplashActivity.this)) {
                                    if (prefrenceHandler.getString(Constans.TYPE).equals("jawwal")) {
                                        GetStatus(getConvertJson("pal_jawwal_meme", "status", prefrenceHandler.getString(Constans.MOBILE_NO),"jawwal"));
                                    } else if (prefrenceHandler.getString(Constans.TYPE).equals("ooredoo")) {
                                        GetStatus(getConvertJson("pal_ooredoo_drawit", "status", prefrenceHandler.getString(Constans.MOBILE_NO),"ooredoo"));
                                    } else {
                                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                } else {
                                    Constans.NoInternetDialog(SplashActivity.this);
                                }
                            } else {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        } else {
                            Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CountryCodeModel> call, Throwable t) {
                call.cancel();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void GetStatus(String object) {
        progressBar.setVisibility(View.VISIBLE);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object));

        final ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.getStatus(body).enqueue(new Callback<SubStatusModel>() {
            @Override
            public void onResponse(Call<SubStatusModel> call, Response<SubStatusModel> response) {
                if (response.isSuccessful()) {
                    SubStatusModel adsDataModel = response.body();
                    if (adsDataModel.getResponse().equals("SUCCESS")) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubStatusModel> call, Throwable t) {
                call.cancel();
                progressBar.setVisibility(View.GONE);
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public String getConvertJson(String app_name, String apiName, String apinumber, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appname", app_name);
            jsonObject.put("apiname", apiName);
            jsonObject.put("msisdn", apinumber);
            if (type.equals("ooredoo")) {
                jsonObject.put("tid", prefrenceHandler.getString(Constans.KEY));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG-->", "getConvertJson: " + jsonObject.toString());
        return jsonObject.toString();
    }
}