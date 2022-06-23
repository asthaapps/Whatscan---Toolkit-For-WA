package com.whatscan.toolkit.forwa.Api;


import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static Retrofit retrofit;

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Preference.getMain_Url()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}