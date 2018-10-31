package com.texaconnect.texa.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.event.MQTTReConnect;
import com.texaconnect.texa.model.User;
import com.texaconnect.texa.network.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;

public class FetchRefreshTokenWorker  extends Worker {

    private static final String TAG = "FetchRefreshTokenWorker";
    ApiInterface apiInterface;

    public FetchRefreshTokenWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
//        DaggerMyApplicationComponent.builder().application(TexaApplication.getApp())
//                .build().inject(this);

    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG,"doWork");
        Call<User> repos = TexaApplication.getApp().getApiInterface().getRefreshedTokens("refresh_token",TexaApplication.sRefreshToken);

        TexaApplication.sIsRefreshingToken = true;
        try {
            Response<User> response = repos.execute();
            if(response.isSuccessful()){
                User appTokens = response.body();
                Log.d(TAG,"doWork "+appTokens.getAccess_token());
                TexaApplication.getApp().saveTokens(appTokens.getAccess_token(), appTokens.getRefresh_token());
//                EventBus.getDefault().post(new MQTTReConnect(TAG));
                TexaApplication.sIsRefreshingToken = false;
                return Result.SUCCESS;

            }
            Log.d(TAG,"doWork failed");

        } catch (IOException e) {
            e.printStackTrace();
        }
        TexaApplication.sIsRefreshingToken = false;
        return Result.FAILURE;
    }

    private void refreshCurrentToken(){

    }
}
