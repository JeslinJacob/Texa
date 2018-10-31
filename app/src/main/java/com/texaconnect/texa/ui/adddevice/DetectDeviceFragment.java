package com.texaconnect.texa.ui.adddevice;


import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentDetectDeviceBinding;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetectDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetectDeviceFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoClearedValue<FragmentDetectDeviceBinding> binding;
    AddDeviceViewModel mAddDeviceViewModel;


    public DetectDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetectDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetectDeviceFragment newInstance(String param1, String param2) {
        DetectDeviceFragment fragment = new DetectDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetectDeviceBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detect_device,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        dataBinding.buttonConnect.setOnClickListener(v -> {

            connectToWifi();
        });
        dataBinding.button.setOnClickListener(v -> {

            navigationController.navigateToConnectDevice();
        });

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddDeviceViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(AddDeviceViewModel.class);
        binding.get().setLifecycleOwner(this);
        binding.get().setModel(mAddDeviceViewModel);

        mAddDeviceViewModel.observeLocalMacId().observe(this, result -> {

            if (result.status == Status.SUCCESS) {
                mAddDeviceViewModel.setDeviceDetected();
            }else if (result.status == Status.ERROR){
                mListener.showToast(result.message);
            }

        });

        mListener.startWifiScan();
        }

    public void connectToWifi(){
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

    }
}
