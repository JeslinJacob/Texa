package com.texaconnect.texa.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.texaconnect.texa.R;
import com.texaconnect.texa.ui.BaseActivity;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LoginActivity extends BaseActivity implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TexaApplication.getApp().initializeUser();
        setContentView(R.layout.activity_login);
        Log.d("LoginActivity","onCreate ");
//        if (user.isSignedIn()) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//            return;
//        }else {
            Intent i = getIntent();
            if(i.getBooleanExtra("isLogin",false)){

               navigationController.navigateToLoginFragment();
            }else {

                navigationController.navigateToSignUp();
            }

//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
