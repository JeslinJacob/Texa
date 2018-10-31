package com.texaconnect.texa.ui.devices;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentDevicesBinding;
import com.texaconnect.texa.interfaces.DeviceClickCallback;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.Node;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "DevicesFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoClearedValue<FragmentDevicesBinding> binding;
    AutoClearedValue<DeviceListAdapter> adapter;
    private MqttViewModel mqttViewModel;
    private boolean isRefreshed;
    private List<Object> items;

    public DevicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesFragment newInstance(String param1, String param2) {
        DevicesFragment fragment = new DevicesFragment();
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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDevicesBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_devices,
                container, false, dataBindingComponent);
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        dataBinding.swiperefresh.setOnRefreshListener(
                () -> {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                    isRefreshed = true;
                    mqttViewModel.getDeviceData();

                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                }
        );

        binding = new AutoClearedValue<>(this, dataBinding);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mqttViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MqttViewModel.class);

        mListener.setTitle("Devices");

        mqttViewModel.observeDeviceDataAll().observe(this, result -> {
            if (isRefreshed) {
                adapter.get().getItems().clear();
                adapter.get().notifyDataSetChanged();
                isRefreshed = false;

            }
            items = result;
            adapter.get().replace(result);

        });
        mqttViewModel.observeDeviceData().observe(this, result -> {
            // update UI

            Log.d(TAG, "observeDeviceData " + result.status);
            if (result.status == Status.SUCCESS) {
                binding.get().swiperefresh.setRefreshing(false);
//                binding.get().progressBar.setVisibility(View.GONE);

                if(result.data == null || result.data.isEmpty()){
                    binding.get().textViewEmpty.setVisibility(View.VISIBLE);
                }else {
                    binding.get().textViewEmpty.setVisibility(View.GONE);
                }


            } else if (result.status == Status.LOADING) {
                binding.get().swiperefresh.setRefreshing(true);
//                binding.get().progressBar.setVisibility(View.VISIBLE);
            } else {
                mListener.showToast(result.message);
                binding.get().swiperefresh.setRefreshing(false);
//                binding.get().progressBar.setVisibility(View.GONE);

            }
        });

        DeviceListAdapter prAdapter = new DeviceListAdapter(dataBindingComponent,
                callback);
//        binding.get().devicesRecyclerView.setLayoutManager(mGridLayoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(mSpanSizeLookup);
        binding.get().devicesRecyclerView.setLayoutManager(gridLayoutManager);
        binding.get().devicesRecyclerView.setAdapter(prAdapter);
        adapter = new AutoClearedValue<>(this, prAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.devices_menu, menu);
        return;
    }

    DeviceClickCallback callback = new DeviceClickCallback() {
        @Override
        public void onDeviceClick(DeviceItem deviceItem) {
            Log.d(TAG, "DeviceClickCallback onDeviceClick " + deviceItem.name);

        }

        @Override
        public void onSwitchClick(Node node) {
            if (node.mode == Node.MODE.DIMMER && node.isAvailable()) {
                navigationController.showIntensityDialog(node.subscriptionTopic);
            } else {
                node.toggleSwitch();
            }

        }
    };

    GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int i) {
            if (items.get(i) instanceof DeviceItem) {
                return 3;
            }else return 1;
        }
    };
}