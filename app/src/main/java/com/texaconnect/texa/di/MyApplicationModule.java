package com.texaconnect.texa.di;

import com.texaconnect.texa.ui.MainActivity;
import com.texaconnect.texa.ui.account.LoginActivity;
import com.texaconnect.texa.ui.account.ProfileActivity;
import com.texaconnect.texa.ui.adddevice.AddDeviceActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MyApplicationModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivityInjector();
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LoginActivity contributeLoginActivityInjector();
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ProfileActivity contributeProfileActivityInjector();
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract AddDeviceActivity contributeAddDeviceActivity();
}
