package com.texaconnect.texa.network;

import android.text.TextUtils;
import android.util.Log;

import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.event.LogoutEvent;
import com.texaconnect.texa.event.MQTTDisconnect;
import com.texaconnect.texa.model.User;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class TexaInterceptor implements Interceptor {
   private static final int HTTP_UNAUTHORIZED_CODE = 401;
   private static final int HTTP_SUCCESS_CODE = 200;
   private static final int HTTP_INVALID_LOGIN_CODE = 400;
   private static final String TAG = "TexaInterceptor";


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
//        String token = TexaApplication.sAccesToken; // get token logic

        /*

        HttpUrl url = null;
        if (originalHttpUrl.host().contains("user")) {
            url = originalHttpUrl.newBuilder()
                    .setQueryParameter("access_token", TexaApplication.sAccesToken)
                    .build();
        }*/

        HttpUrl originalHttpUrl = originalRequest.url();

        String type = null;
        Log.d(TAG,"intercept path "+originalHttpUrl.url().getPath());
        if (!originalHttpUrl.url().getPath().contains("auth/oauth/token")) {
            type = TexaApplication.getApp().getTokenType();
        }else {
            type = "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=";

        }

        Request newRequest = originalRequest.newBuilder()
//                .header("Authorization", "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=")
                .header("Authorization", type)
                .header("x-client-device", "Android")
                .header("Content-Type", "application/json")
//                .header("Accept", "application/json")
                .header("location-code", "IN")
                .header("x-device-id", TexaApplication.sDeviceId)
//                .url(url)
                .build();



        Response response;

        /*final URI uri = chain.request().url().uri();
        String url = uri.getPath();

        if(url.contains("services/v1/user/devices")){
            String responseString = DummyData.getDummyDevices();
            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        }else*/

        response = chain.proceed(newRequest);


        Log.d(TAG,"intercept "+response.code()+", "+newRequest);
        if (response.code() == HTTP_UNAUTHORIZED_CODE &&
                TexaApplication.getApp().isLoggedIn() && !TexaApplication.sIsRefreshingToken) {

            Response r = null;

            Log.d(TAG,"intercept unauthorized sAccesToken "+ TexaApplication.sAccesToken);
            r = makeTokenRefreshCall(newRequest, chain);
            Log.d(TAG,"intercept unauthorized refreshed sAccesToken "+ TexaApplication.sAccesToken);

            return r;

            // Session expired or logout or refresh token
        }else if(response.code() == HTTP_INVALID_LOGIN_CODE){
//            EventBus.getDefault().post(new LogoutEvent());
            Log.d(TAG,"intercept invalid sAccesToken "+ TexaApplication.sAccesToken);

        } else {
            Log.d(TAG,"intercept else sAccesToken "+ TexaApplication.sAccesToken);
            Log.d(TAG,"intercept else sRefreshToken "+ TexaApplication.sRefreshToken);

            // Store values.
//            String cookie = response.headers().get("Set-Cookie");
//            String session = response.headers().get("Api-Session");
//            String token2 = response.headers().get("Token");

            }

        return response;
    }

    public Response makeTokenRefreshCall(Request req, Chain chain) throws IOException{
        Log.d(TAG,"makeTokenRefreshCall "+req.toString());
//        EventBus.getDefault().post(new MQTTDisconnect());

        TexaApplication.sIsRefreshingToken = true;
        String newToken = refreshToken();
        TexaApplication.sIsRefreshingToken = false;

        Log.d(TAG,"makeTokenRefreshCall newToken "+newToken);

        /*HttpUrl originalHttpUrl = req.url();

        HttpUrl url = originalHttpUrl.newBuilder()
//                .addQueryParameter("access_token", newToken)
                .setQueryParameter("access_token", newToken)
                .build();*/

        Request newRequest = req.newBuilder()
//                .header("Authorization", "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=")
                .header("Authorization", TexaApplication.getApp().getTokenType())
//                .header("Content-Type", "application/json")
//                .header("location-code", "IN")
//                .header("x-device-id", TexaApplication.sDeviceId)
//                .url(url)
                .build();
        Response another =  chain.proceed(newRequest);

        if (!(another.code() >= 200 && another.code() < 300)) {
//            makeTokenRefreshCall(newRequest, chain);
            EventBus.getDefault().post(new LogoutEvent());

        }

            return another;

    }

    public String refreshToken(){
        Log.d(TAG,"refreshToken");
        Call<User> repos = TexaApplication.getApp().getApiInterface().getRefreshedTokens("refresh_token",TexaApplication.sRefreshToken);
        try {
            retrofit2.Response<User> response = repos.execute();

            if(response.isSuccessful()){
                Log.d(TAG,"refreshToken Successful");

                User appTokens = response.body();
                TexaApplication.getApp().saveTokens(appTokens.getAccess_token(), appTokens.getRefresh_token());
                return appTokens.getAccess_token();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}