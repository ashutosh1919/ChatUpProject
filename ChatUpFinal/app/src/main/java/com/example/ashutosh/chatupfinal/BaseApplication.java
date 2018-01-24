package com.example.ashutosh.chatupfinal;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.ashutosh.chatupfinal.interfaces.APIService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class BaseApplication extends Application
{
    private APIService apiService;
    private static BaseApplication defaultContext;
    //public static FirebaseDatabase mDatabase;
    //public static FirebaseStorage mStorage;

    public static BaseApplication getDefaultContext() {
        return defaultContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseApp.initializeApp(this /*context */, FirebaseOptions.fromResource(this) /* context */);
        //mDatabase.getInstance();
        //mStorage.getInstance();
        //if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //}
        defaultContext = this;
    }

    public APIService getApiService() {

        if (apiService == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request.Builder requestBuilder = chain.request().newBuilder();
                            requestBuilder.addHeader("Accept", "application/json");
                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(getString(R.string.demo_url))
                    .baseUrl(getString(R.string.user_server_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

}
