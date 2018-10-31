package com.texaconnect.texa.ui.intro_pages;

import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.texaconnect.texa.R;
import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.event.MQTTConnect;
import com.texaconnect.texa.ui.BaseActivity;
import com.texaconnect.texa.ui.MainActivity;
import com.texaconnect.texa.util.MyUtils;

import org.greenrobot.eventbus.EventBus;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!TexaApplication.getApp().isLoggedIn()){
            Intent intent = new Intent(SplashActivity.this, WelcomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else {
//            mqttViewModel.getDeviceData();

            if (MyUtils.isNetworkAvailable(this) && TexaApplication.getApp().isLoggedIn()) {
                EventBus.getDefault().post(new MQTTConnect());
            }

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        new Handler().postDelayed(() -> {
////            User user = TexaApplication.getApp().currentUser();
////            if (!user.isSignedIn()) {
//            if(!TexaApplication.getApp().isLoggedIn()){
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//
//            }else {
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

//        Crashlytics.getInstance().crash(); // Force a crash
//        FirebaseCrash.log("Activity created");


    }
}
