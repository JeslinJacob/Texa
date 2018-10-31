package com.texaconnect.texa.ui.adddevice;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.texaconnect.texa.R;
import com.texaconnect.texa.interfaces.PermissionCallback;
import com.texaconnect.texa.ui.BaseActivity;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class AddDeviceActivity extends BaseActivity implements HasSupportFragmentInjector {

    private static final String TAG = "AddDeviceActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private WifiManager mWifiManager;
    AddDeviceViewModel mAddDeviceViewModel;
    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_gray_24dp);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(mWifiScanReceiver,
                intentFilter);

        if (savedInstanceState == null) {
            navigationController.navigateToConnectDevice();
        }
        mAddDeviceViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddDeviceViewModel.class);
        mWifiManager.setWifiEnabled(true);

//        WifiInfo info = mWifiManager.getConnectionInfo();
//        Log.d(TAG, "wifi ssid "+ info.getSSID());
//        mAddDeviceViewModel.setSSID(info.getSSID());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiScanReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkLocationPermission();

    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] location_perm = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };
            onRequestPermission(new PermissionCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed() {

                }
            }, location_perm);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_device_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void startWifiScan() {
        super.startWifiScan();
        mWifiManager.startScan();

    }

    private String getRouterIPAddress() {
        DhcpInfo dhcp = mWifiManager.getDhcpInfo();
        int ip = dhcp.gateway;
        return formatIP(ip);
    }

    private String formatIP(int ip) {
        return String.format(Locale.getDefault(),
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff)
        );
    }

    private void setScanResults() {
        List<ScanResult> mScanResults = mWifiManager.getScanResults();

        mAddDeviceViewModel.setScanResults(mScanResults);

//        for (ScanResult scanResult : mScanResults) {
//            Log.d(TAG, "ScanResult " + scanResult);
//                    Log.d("MainActivity", "ScanResult " + scanResult.SSID);
//                    Log.d("MainActivity", "ScanResult " + scanResult.level);
//                    Log.d("MainActivity", "ScanResult " +
//                            WifiManager.calculateSignalLevel(scanResult.level, 100));
//        }
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            Log.d(TAG, "BroadcastReceiver onReceive " + intent);

            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                setScanResults();

            }else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                mWifiManager.startScan();

                NetworkInfo networkInfo =
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected()) {
                    //do stuff
                    WifiInfo info = mWifiManager.getConnectionInfo();
                    Log.d(TAG, "wifi connected "+info.getSSID());
                    mAddDeviceViewModel.getLocalMacID(info.getSSID(), getRouterIPAddress());
                }else {
                    Log.d(TAG, "wifi connection was lost");

                }

                /*if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){

                } else {
                    // wifi connection was lost

                }*/
            }else if(intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
                mWifiManager.startScan();

                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                    WifiInfo info = mWifiManager.getConnectionInfo();
                    Log.d(TAG, "wifi connected "+info.getSSID());
                    mAddDeviceViewModel.getLocalMacID(info.getSSID(), getRouterIPAddress());
                } else {
                    // wifi connection was lost
                    Log.d(TAG, "wifi connection was lost");

                }
            }
        }
    };

}