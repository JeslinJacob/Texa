package com.texaconnect.texa.network;

import android.app.Application;

import com.texaconnect.texa.BuildConfig;
import com.texaconnect.texa.persistentcookiejar.PersistentCookieJar;
import com.texaconnect.texa.persistentcookiejar.cache.SetCookieCache;
import com.texaconnect.texa.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.texaconnect.texa.util.LiveDataCallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {

    public static OkHttpClient getClient(Application app) {
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(app));
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            client.addInterceptor(interceptor);

            client.addInterceptor(new TexaInterceptor());
//            client.cookieJar(cookieJar);

        return client.build();
    }

    public static ApiInterface getApiInterface(Application app){

        return new Retrofit.Builder()

                .baseUrl("https://www.texaconnect.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(RestClient.getClient(app))
                .build()
                .create(ApiInterface.class);
    }
}