package com.texaconnect.texa.ui.dialogs;

import android.arch.lifecycle.ViewModelProviders;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.texaconnect.texa.R;
import com.texaconnect.texa.event.wifiListEvent;
import com.texaconnect.texa.model.SelectedWifiItem;
import com.texaconnect.texa.ui.BaseDialogFragment;
import com.texaconnect.texa.ui.adddevice.AddDeviceViewModel;
import com.texaconnect.texa.ui.adddevice.WifiListListAdapter;
import com.texaconnect.texa.ui.adddevice.WifiListRecyclerViewAdapter;
import com.texaconnect.texa.util.AutoClearedValue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ScanWifiFragment extends BaseDialogFragment {

    private List<SelectedWifiItem> mSelectedWifiItems;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button refresh;

    AddDeviceViewModel mAddDeviceViewModel;
    AutoClearedValue<WifiListListAdapter> adapter;

    public static ScanWifiFragment newInstance(){
        ScanWifiFragment dialog = new ScanWifiFragment();
        Bundle args = new Bundle();
//        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_scan_wifi, container, false);
        refresh = view.findViewById(R.id.refresh_btn);
        recyclerView = view.findViewById(R.id.wifi_recycler_list);
        progressBar=view.findViewById(R.id.scan_progress);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mListener.startWifiScan();
            }
        });
//        initializeData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddDeviceViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(AddDeviceViewModel.class);
        mAddDeviceViewModel.observeScanResults().observe(this, result -> {
            mSelectedWifiItems = new ArrayList<>();

            for(ScanResult scanResult : result){
//                    Log.d("MainActivity","ScanResult "+scanResult);
//                    Log.d("MainActivity","ScanResult "+scanResult.SSID);
//                    Log.d("MainActivity","ScanResult "+scanResult.level);
//                    Log.d("MainActivity","ScanResult "+WifiManager.calculateSignalLevel(scanResult.level,100));
                mSelectedWifiItems.add(new SelectedWifiItem(scanResult.SSID,security(scanResult.capabilities)));
            }

//            EventBus.getDefault().post(new wifiListEvent(mSelectedWifiItems));

            WifiListRecyclerViewAdapter recyclerViewAdapter = new WifiListRecyclerViewAdapter(mSelectedWifiItems,getContext());
            recyclerView.setAdapter(recyclerViewAdapter);
            progressBar.setVisibility(View.GONE);
            refresh.setEnabled(true);

            adapter.get().replace(mSelectedWifiItems);

            // add your logic here

        });

        WifiListListAdapter prAdapter = new WifiListListAdapter(dataBindingComponent,
                mWifiSelected);
        recyclerView.setAdapter(prAdapter);
        adapter = new AutoClearedValue<>(this, prAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.startWifiScan();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(wifiListEvent wifiListEvent){

    }

    public String security(String cap){
        final String WPA = "WPA";
        final String WEP = "WEP";
        final String WPA2 = "WPA2";
        final String OPEN = "Open";


        if (cap.toLowerCase().contains(WEP.toLowerCase()))
        {return WEP ;}

        else if (cap.toLowerCase().contains(WPA2.toLowerCase()))
        {return WPA2;}

        else if (cap.toLowerCase().contains(WPA.toLowerCase()))
        {return WPA;}
        else return OPEN;

    }

    WifiListListAdapter.WifiSelected mWifiSelected = new WifiListListAdapter.WifiSelected() {
        @Override
        public void onWifiSelected(SelectedWifiItem wifiItem) {
            mAddDeviceViewModel.setSelectedWiFi(wifiItem);
            dismiss();
        }
    };
}
