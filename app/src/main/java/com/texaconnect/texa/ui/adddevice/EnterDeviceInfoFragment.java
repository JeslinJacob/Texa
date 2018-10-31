package com.texaconnect.texa.ui.adddevice;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentEnterDeviceInfoBinding;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterDeviceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterDeviceInfoFragment extends BaseFragment implements TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "EnterDeviceInfoFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoClearedValue<FragmentEnterDeviceInfoBinding> binding;
    AddDeviceViewModel mAddDeviceViewModel;

    public EnterDeviceInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterDeviceInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterDeviceInfoFragment newInstance(String param1, String param2) {
        EnterDeviceInfoFragment fragment = new EnterDeviceInfoFragment();
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
        FragmentEnterDeviceInfoBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_device_info,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        dataBinding.button.setOnClickListener(v -> {
          sendAndVerify();
        });

        binding.get().editTextName.addTextChangedListener(this);
        binding.get().editTextSerial.addTextChangedListener(this);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddDeviceViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(AddDeviceViewModel.class);

        mAddDeviceViewModel.observeMacId().observe(this, result -> {
            Log.d(TAG, "observeMacId " + result.status);
            if (result.status == Status.SUCCESS) {
                binding.get().progressBar.setVisibility(View.GONE);
                navigationController.navigateToDetectDevice();

            }else if (result.status == Status.LOADING) {
                binding.get().progressBar.setVisibility(View.VISIBLE);

            }else {
                binding.get().progressBar.setVisibility(View.GONE);
                mListener.showToast(result.message);
                binding.get().serialWrapper.setError(result.message);
            }

        });

    }

    private void sendAndVerify() {

        String name = binding.get().editTextName.getText().toString();
        String serial = binding.get().editTextSerial.getText().toString();

        boolean error = false;

        if(TextUtils.isEmpty(name)){
            error = true;
            binding.get().nameWrapper.setError(getString(R.string.error_dev_enter_name));
        }else if(TextUtils.isEmpty(serial)){
            error = true;
            binding.get().serialWrapper.setError(getString(R.string.error_dev_serial));
        }

        if(!error){
            mAddDeviceViewModel.verifySerial(name, serial);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String name = binding.get().editTextName.getText().toString();
        String serial = binding.get().editTextSerial.getText().toString();

        binding.get().button.setEnabled(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(serial));
    }
}