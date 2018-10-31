package com.texaconnect.texa.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.texaconnect.texa.R;
import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.event.LogoutEvent;
import com.texaconnect.texa.event.MQTTDisconnect;
import com.texaconnect.texa.event.MQTTReConnect;
import com.texaconnect.texa.model.Status;

import com.texaconnect.texa.ui.account.ProfileActivity;

import com.texaconnect.texa.ui.adddevice.AddDeviceActivity;

import com.texaconnect.texa.ui.devices.MqttViewModel;
import com.texaconnect.texa.util.NetworkStateReceiver;
import com.texaconnect.texa.util.NetworkStateReceiver.NetworkStateReceiverListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector, FragmentManager.OnBackStackChangedListener, NetworkStateReceiverListener {

    private static final String TAG = "MainActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
//    @Inject
//    User mUser;
    private MqttViewModel mqttViewModel;
    @Inject
    protected ViewModelProvider.Factory viewModelFactory;
    protected NetworkStateReceiver networkStateReceiver;

//    @Inject
//    NavigationController navigationController;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        navigationController.popBackAll();
        switch (item.getItemId()) {
            case R.id.navigation_dash_board:
                navigationController.navigateToDashBoard();
                return true;
            case R.id.navigation_devices:
                navigationController.navigateToDevicesFragment();
                return true;
            case R.id.navigation_automate:
                return true;
            case R.id.navigation_notifications:
                Intent i = new Intent(this,ProfileActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    };
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        mqttViewModel = ViewModelProviders.of(this, viewModelFactory).get(MqttViewModel.class);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_gray_24dp);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitleTextAppearance(this, R.style.RobotoTextAppearance);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);

        TextView textViewName = (TextView) view.findViewById(R.id.textView1);
        textViewName.setText(TexaApplication.getApp().getCurrentUser().userData.name);

        findViewById(R.id.toolbar).setOnClickListener(v -> {

        });
        /*mUser.observeUser().observe(this, result -> {
            // update UI

            if (!result.isSignedIn()) {
                Log.d(TAG, "Signed out ");
                Toast.makeText(this, "Signed out ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
            }

        });*/

        mqttViewModel.observeDeviceData().observe(this, result -> {
            // update UI
            Log.d(TAG, "observeDeviceData " + result.status);
            if (savedInstanceState == null && result.status == Status.SUCCESS) {
                if (result.data != null) {
                    mqttViewModel.setSubscription();
                }
                mqttViewModel.getGroupData();

            }
        });

        mqttViewModel.observeGroupData().observe(this, result -> {
            // update UI
            Log.d(TAG, "observeDeviceData " + result.status);
            if (savedInstanceState == null && result.status == Status.SUCCESS) {
                mqttViewModel.addItems(result.data);
            }
        });

        if (savedInstanceState == null) {
            navigationController.navigateToDashBoard();
//            navigation.setSelectedItemId(R.id.navigation_devices);
            EventBus.getDefault().post(new MQTTReConnect(TAG+" onCreate"));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
        EventBus.getDefault().post(new MQTTDisconnect());

    }

    @Override
    public void networkAvailable() {

        disMissAlert();
        EventBus.getDefault().post(new MQTTReConnect(TAG +" networkAvailable"));
        if (mqttViewModel.observeDeviceData().getValue() == null) {
            mqttViewModel.getDeviceData();
        }
    }

    @Override
    public void networkUnavailable() {
        showAlert("Network issue","Please turn on your internet connection to use the app");
//        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manu_main, menu);

//        return super.onCreateOptionsMenu(menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "onOptionsItemSelected getItemId " + item.getItemId());
        switch (item.getItemId()) {
//            case android.R.id.home:
//                return true;

//            case R.id.logout:
//                TexaApplication.sAccesToken = "";
//                return true;

            case R.id.add_group:
                navigationController.showAddGroupDialog();
                return true;
            case R.id.add_device:

                startActivity(new Intent(this, AddDeviceActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);

    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LogoutEvent event) {
        Log.d(TAG,"onEvent LogoutEvent");
        logout(null);
    }

    public void logout(View view) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        TexaApplication.getApp().logout();
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        EventBus.getDefault().post(new MQTTDisconnect());
        showToast(getString(R.string.logged_out));
        finish();
    }
}