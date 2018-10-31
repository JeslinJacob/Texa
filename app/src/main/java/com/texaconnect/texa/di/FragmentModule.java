package com.texaconnect.texa.di;

import com.texaconnect.texa.ui.account.FinishRegistrationFragment;
import com.texaconnect.texa.ui.account.ForgetPasswordFragment;
import com.texaconnect.texa.ui.account.LoginFragment;
import com.texaconnect.texa.ui.account.ProfileFragment;
import com.texaconnect.texa.ui.account.SignUpFragment;
import com.texaconnect.texa.ui.adddevice.ConnectWifiFragment;
import com.texaconnect.texa.ui.adddevice.DetectDeviceFragment;
import com.texaconnect.texa.ui.adddevice.EnterDeviceInfoFragment;
import com.texaconnect.texa.ui.adddevice.SetupCompleteFragment;
import com.texaconnect.texa.ui.devices.AddGroupFragment;
import com.texaconnect.texa.ui.devices.DashBoardFragment;
import com.texaconnect.texa.ui.devices.DevicesFragment;
import com.texaconnect.texa.ui.dialogs.ScanWifiFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract SignUpFragment contributeSignUpFragmentInjector();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragmentInjector();
    @ContributesAndroidInjector
    abstract ForgetPasswordFragment contributeForgetPasswordFragmentInjector();
    @ContributesAndroidInjector
    abstract DashBoardFragment contributeDashBoardFragmentInjector();
    @ContributesAndroidInjector
    abstract DevicesFragment contributeDevicesFragmentInjector();
    @ContributesAndroidInjector
    abstract FinishRegistrationFragment contributeFinishRegistrationFragmentInjector();
    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();
    @ContributesAndroidInjector
    abstract AddGroupFragment contributeAddGroupFragmentInjector();
    @ContributesAndroidInjector
    abstract EnterDeviceInfoFragment contributeEnterDeviceInfoFragmentInjector();
    @ContributesAndroidInjector
    abstract DetectDeviceFragment contributeDetectDeviceFragmentInjector();
    @ContributesAndroidInjector
    abstract ConnectWifiFragment contributeConnectDeviceFragmentInjector();
    @ContributesAndroidInjector
    abstract SetupCompleteFragment contributeSetupCompleteFragmentInjector();
    @ContributesAndroidInjector
    abstract ScanWifiFragment contributeScanWifiFragmentInjector();
}
