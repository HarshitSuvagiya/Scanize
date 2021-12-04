package com.girls.scanize.selfiecamera.Services;


import com.girls.scanize.selfiecamera.Utils.Constans;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;
    private static Retrofit retrofit2 = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constans.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create((new GsonBuilder().serializeNulls().create())))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClient1() {

        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(Constans.COUNTRYCODEURL)
                    .addConverterFactory(GsonConverterFactory.create((new GsonBuilder().serializeNulls().create())))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }

    public static Retrofit getClient2() {

        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(Constans.TOCKENURL)
                    .addConverterFactory(GsonConverterFactory.create((new GsonBuilder().serializeNulls().create())))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }


}
