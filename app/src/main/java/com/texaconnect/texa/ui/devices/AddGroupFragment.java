package com.texaconnect.texa.ui.devices;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.texaconnect.texa.R;
import com.texaconnect.texa.binding.FragmentDataBindingComponent;
import com.texaconnect.texa.databinding.FragmentAddGroupBinding;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.util.AutoClearedValue;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGroupFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<FragmentAddGroupBinding> binding;
    private MqttViewModel mqttViewModel;
    @Inject
    protected ViewModelProvider.Factory viewModelFactory;


    public AddGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddGroupFragment newInstance(String param1, String param2) {
        AddGroupFragment fragment = new AddGroupFragment();
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
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentAddGroupBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_group,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        dataBinding.button.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(dataBinding.editText.getText().toString())){
                mqttViewModel.addGroup(dataBinding.editText.getText().toString());
            }else {
                Toast.makeText(getActivity(),"Enter a Group name!",Toast.LENGTH_LONG).show();

            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mqttViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MqttViewModel.class);

        mqttViewModel.observeAddGroupData().observe(this, result -> {
            if (result.status == Status.SUCCESS) {
                binding.get().progressLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(),result.data,Toast.LENGTH_LONG).show();

                mqttViewModel.getGroupData();
                dismiss();

            } else if (result.status == Status.LOADING) {
                binding.get().progressLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(),result.message,Toast.LENGTH_LONG).show();
                binding.get().progressLayout.setVisibility(View.GONE);
            }

        });

    }
}