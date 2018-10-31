package com.texaconnect.texa.network;

import com.google.gson.JsonObject;
import com.texaconnect.texa.model.AddGroup;
import com.texaconnect.texa.model.AddGroupResponse;
import com.texaconnect.texa.model.AppTokens;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.GroupItem;
import com.texaconnect.texa.model.OTPVerifyRequest;
import com.texaconnect.texa.model.ProfileUpdateRequest;
import com.texaconnect.texa.model.SignUpRequest;
import com.texaconnect.texa.model.User;
import com.texaconnect.texa.model.UserData;
import com.texaconnect.texa.ui.adddevice.AddDeviceViewModel;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("auth/oauth/token")
    Call<User> login(@Query("grant_type") String grantType,
                     @Query("username") String username ,
                     @Query("password") String password);

    @POST("auth/oauth/token")
    Call<User> getRefreshedTokens(@Query("grant_type") String grantType,
                                       @Query("refresh_token") String refreshToken);

    @GET("services/v1/user/")
    Call<UserData> getUserProfile();

    @POST("services/v1/user")
    Call<ResponseBody> signUp(@Body SignUpRequest signUpRequest);

    @GET("/forgot-password")
    Call<ResponseBody> forgotPassword(@Query("email") String email);

    @PUT("services/v1/user/")
    Call<ResponseBody> updateProfile(@Body ProfileUpdateRequest updateRequest);

    @POST("/otp-verify")
    Call<ResponseBody> verifyOtp(@Body OTPVerifyRequest otpVerifyRequest);

    @GET("services/v1/user/devices")
    Call<List<DeviceItem>> getDevices();

    @GET("services/v1/user/groups")
    Call<List<GroupItem>> getGroups();

    @POST("services/v1/user/groups")
    Call<AddGroupResponse> addGroup(@Body AddGroup group);

    @POST("services/v1/user/devices/validate_device")
    Call<String> verifySerial(@Body AddDeviceViewModel.DeviceInfo group);

    @GET("{ip}/__SL_G_N.D")
    Call<String> getLocalMACId2(@Query("ip") String ip);


    @GET
    Call<String> getLocalMACId(@Url String ip);
}
