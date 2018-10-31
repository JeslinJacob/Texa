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
import com.texaconnect.texa.databinding.FragmentDashBoardBinding;
import com.texaconnect.texa.interfaces.GroupClickCallback;
import com.texaconnect.texa.model.Element;
import com.texaconnect.texa.model.GroupItem;
import com.texaconnect.texa.model.Node;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "DashBoardFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoClearedValue<FragmentDashBoardBinding> binding;
    AutoClearedValue<GroupListAdapter> adapter;
    private MqttViewModel mqttViewModel;
    private List<Object> items;
    private boolean isRefreshed;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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

        FragmentDashBoardBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board,
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
        mListener.setTitle("Dashboard");

        mqttViewModel.observeGroupDataAll().observe(this, result -> {
            // update UI

            Log.d(TAG, "observeDeviceData ");
            if (isRefreshed) {
                adapter.get().getItems().clear();
                adapter.get().notifyDataSetChanged();
                isRefreshed = false;

            }
            items = result;
            adapter.get().replace(result);

        });

        mqttViewModel.observeGroupData().observe(this, result -> {
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

            }else if (result.status == Status.LOADING) {
                binding.get().swiperefresh.setRefreshing(true);
//                binding.get().progressBar.setVisibility(View.VISIBLE);
            }else {

                mListener.showToast(result.message);
                binding.get().swiperefresh.setRefreshing(false);
//                binding.get().progressBar.setVisibility(View.GONE);

            }
        });

        GroupListAdapter prAdapter = new GroupListAdapter(dataBindingComponent,
                callback);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(mSpanSizeLookup);
        binding.get().groupsRecyclerView.setLayoutManager(gridLayoutManager);
        binding.get().groupsRecyclerView.setAdapter(prAdapter);
        adapter = new AutoClearedValue<>(this, prAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.dash_menu, menu);
        return;
    }

    GroupClickCallback callback = new GroupClickCallback() {
        @Override
        public void onGroupClick(GroupItem groupItem) {
            Log.d(TAG, "GroupClickCallback onGroupClick " + groupItem.name);

        }

        @Override
        public void onElementClick(Element element) {
            Log.d(TAG, "GroupClickCallback onElementClick " + element.node.isEnabled);

            if (element.node.mode == Node.MODE.DIMMER && element.node.isAvailable()) {
                navigationController.showIntensityDialog(element.node.subscriptionTopic);
            } else {
                element.node.toggleSwitch();
            }
        }
    };

    GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int i) {
            if (items.get(i) instanceof GroupItem) {
                return 3;
            }else return 1;
        }
    };
}