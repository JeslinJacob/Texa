package com.texaconnect.texa;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.texaconnect.texa.di.AppInjector;
import com.texaconnect.texa.event.MQTTConnect;
import com.texaconnect.texa.event.MQTTReConnect;
import com.texaconnect.texa.model.User;
import com.texaconnect.texa.mqtt.MQTTManager;
import com.texaconnect.texa.network.ApiInterface;
import com.texaconnect.texa.util.Constantz;
import com.texaconnect.texa.util.MyUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.fabric.sdk.android.Fabric;

public class TexaApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private static TexaApplication mTexaApplication;
    private SharedPreferences mPreferences;
    public static String sDeviceId;
    public static String sAccesToken;
    public static String sRefreshToken;
    public static String sUsername;
    public static String sPassword;
    public static boolean sIsRefreshingToken;
    User mCurrentUser;
    @Inject
    MQTTManager mMQTTManager;
    @Inject
    ApiInterface apiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

//        DaggerMyApplicationComponent.create().inject(this);
//        MqttViewModel myViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(MqttViewModel.class);
        sDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        AppInjector.init(this);
        Fabric.with(this, new Crashlytics());

        mTexaApplication = this;
        mPreferences = getSharedPreferences(Constantz.PREFERENCE_NAME, Context.MODE_PRIVATE);
        initializeUser();
        sAccesToken = mPreferences.getString(Constantz.KEY_ACCESS_TOKEN,"");
        sRefreshToken = mPreferences.getString(Constantz.KEY_REFRESH_TOKEN,"");
        sUsername = mPreferences.getString(Constantz.KEY_USER_NAME,"");
        sPassword = mPreferences.getString(Constantz.KEY_PASSWORD,"");
//        ViewTarget.setTagId(R.id.glide_tag);
        createNotificationChannel();

//        EventBus.getDefault().register(this);
//        FirebaseCrash.log("Activity created");

    }

    public static TexaApplication getApp() {
        return mTexaApplication;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public SharedPreferences getPreference() {
        if (mPreferences == null)
            mPreferences = getSharedPreferences(Constantz.PREFERENCE_NAME, 0);
        return mPreferences;
    }

    public void saveTokens(String accessToken, String refreshToken){
        sAccesToken = accessToken;
        sRefreshToken = refreshToken;
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.putString(Constantz.KEY_ACCESS_TOKEN, accessToken);
        prefEditor.putString(Constantz.KEY_REFRESH_TOKEN, refreshToken);
        prefEditor.apply();
        EventBus.getDefault().post(new MQTTReConnect("TexaApplication"));
    }

    public String getTokenType(){

        return (getCurrentUser() == null || TextUtils.isEmpty(getCurrentUser().tokenType)) ? "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=" : getCurrentUser().tokenType+" "+sAccesToken;
    }

    public void saveUserCredentials(String email, String password){
        sUsername = email;
        sPassword = password;
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.putString(Constantz.KEY_USER_NAME, email);
        prefEditor.putString(Constantz.KEY_PASSWORD, password);
        prefEditor.apply();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public String getUserId() {
        return mCurrentUser.userData != null ? ""+mCurrentUser.userData.id : "";
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
        persistUser();
    }

    public void logout() {
        sAccesToken = "";
        sRefreshToken = "";
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.clear();
        prefEditor.apply();
        persistUser();

    }

    public boolean isLoggedIn(){
       return mPreferences.getBoolean("loggedIn",false);
    }

    public void setLoggedIn(boolean status, User user){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean("loggedIn",status).apply();
        setCurrentUser(user);

    }

    public void initializeUser() {
        Gson gson = new Gson();
        Log.d("TexaApplication","initializeUser "+mPreferences.getString(Constantz.KEY_CURRENT_USER, "{}"));

        User user = gson.fromJson(mPreferences.getString(Constantz.KEY_CURRENT_USER, "{}"), User.class);
        mCurrentUser = user;
    }

    private void persistUser() {
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        Gson gson = new Gson();
        if (isLoggedIn()) {
            String userString = gson.toJson(mCurrentUser);
            prefEditor.putString(Constantz.KEY_CURRENT_USER, userString);
        } else {
            prefEditor.remove(Constantz.KEY_CURRENT_USER);
            mCurrentUser = null;
        }
        prefEditor.apply();

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(BuildConfig.APPLICATION_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}