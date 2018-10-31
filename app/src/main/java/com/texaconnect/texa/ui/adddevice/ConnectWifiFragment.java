package com.texaconnect.texa.ui.adddevice;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentConnectWifiBinding;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectWifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectWifiFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoClearedValue<FragmentConnectWifiBinding> binding;
    AddDeviceViewModel mAddDeviceViewModel;


    public ConnectWifiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectWifiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectWifiFragment newInstance(String param1, String param2) {
        ConnectWifiFragment fragment = new ConnectWifiFragment();
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
        FragmentConnectWifiBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect_wifi,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        dataBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] mArray = getResources().getStringArray(R.array.wifi_security_types);

                mAddDeviceViewModel.setSecurityType(mArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataBinding.buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationController.showScanWifiList();
            }
        });

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddDeviceViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(AddDeviceViewModel.class);
        mAddDeviceViewModel.observeSecurityType().observe(this, result -> {
            binding.get().passWrapper.setVisibility(result.equals("None") ? View.GONE : View.VISIBLE);

        });

        mAddDeviceViewModel.observeSelectedWiFi().observe(this, result -> {


        });

    }
}
