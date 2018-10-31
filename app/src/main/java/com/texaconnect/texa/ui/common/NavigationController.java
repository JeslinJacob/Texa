package com.texaconnect.texa.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.texaconnect.texa.R;
import com.texaconnect.texa.ui.MainActivity;
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
import com.texaconnect.texa.ui.devices.IntensityDialog;
import com.texaconnect.texa.ui.dialogs.ScanWifiFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NavigationController {

    protected final int containerId;
    protected final FragmentManager fragmentManager;


    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void popOneStepBack() {
        fragmentManager.popBackStack();
    }

    public void popBackAll() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public NavigationController(FragmentActivity activity) {
        this.containerId = R.id.container;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void navigateToLoginFragment() {
        popBackAll();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, loginFragment)
                .commitAllowingStateLoss();
    }


    public void navigateToSignUp() {
        popBackAll();
        Fragment signUpFragment = new SignUpFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, signUpFragment)
                .commit();
    }

    public void navigateToDashBoard() {
        Fragment  dashBoardFragment = new DashBoardFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, dashBoardFragment)
                .commit();
    }

    public void navigateToDevicesFragment() {
        Fragment  devicesFragment = new DevicesFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, devicesFragment)
                .commit();
    }

    public void navigateToProfileFragment() {
        Fragment  profileFragment = new ProfileFragment();
        popBackAll();
        fragmentManager.beginTransaction()
                .replace(containerId, profileFragment)
                .commit();
    }

    public void closeActionRegistration(){
        fragmentManager.popBackStack();
        navigateToLoginFragment();
    }

    public void navigateToForgotPassword(){
        Fragment forgotPassFrag = new ForgetPasswordFragment();
        fragmentManager.beginTransaction()
          .replace(containerId,forgotPassFrag)
          .commit();
    }

    public void navigateToFinishPage(String emailStr){

        Bundle bundle = new Bundle();
        bundle.putString("email",emailStr);
        fragmentManager.popBackStack();
        Fragment finishFragment = new FinishRegistrationFragment();
        finishFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(containerId,finishFragment)
                .commit();
    }

    public void showIntensityDialog(String topic) {
        // Create the fragment and show it as a dialog.
        IntensityDialog newFragment = IntensityDialog.newInstance(topic);
        newFragment.show(fragmentManager, "Intensity");
    }

    public void showScanWifiList() {
        // Create the fragment and show it as a dialog.
        ScanWifiFragment newFragment = ScanWifiFragment.newInstance();
        newFragment.show(fragmentManager, "Intensity");
    }

    public void showAddGroupDialog() {
        // Create the fragment and show it as a dialog.
        AddGroupFragment newFragment = AddGroupFragment.newInstance("", "");
        newFragment.show(fragmentManager, "Add Group");
    }

    public void navigateToEnterDeviceInfo() {
        Fragment  infoFragment = new EnterDeviceInfoFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, infoFragment)
                .commit();
    }

    public void navigateToDetectDevice() {
        Fragment  detectDeviceFragment = new DetectDeviceFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, detectDeviceFragment)
                .commit();
    }

    public void navigateToConnectDevice() {
        Fragment  connectDeviceFragment = new ConnectWifiFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, connectDeviceFragment)
                .commit();
    }

    public void navigateToSetupComplete() {
        Fragment  setupCompleteFragment = new SetupCompleteFragment();
        fragmentManager.beginTransaction()
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
                .replace(containerId, setupCompleteFragment)
                .commit();
    }


}