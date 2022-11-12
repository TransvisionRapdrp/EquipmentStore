package com.example.equipmentstore.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient1 {

    private static final String BASE_URL = "http://bc_service2.hescomtrm.com/TicketingService.asmx/";
    private RegisterApi registerAPI;

    private static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(5, TimeUnit.MINUTES);
        client.readTimeout(5, TimeUnit.MINUTES);
        client.writeTimeout(5, TimeUnit.MINUTES);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public  RegisterApi getApiService() {
        if (registerAPI == null)
            registerAPI = getRetrofitInstance().create(RegisterApi.class);
        return registerAPI;
    }
}
