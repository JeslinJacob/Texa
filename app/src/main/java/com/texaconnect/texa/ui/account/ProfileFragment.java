package com.texaconnect.texa.ui.account;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.heetch.countrypicker.Country;
import com.heetch.countrypicker.CountryPickerCallbacks;
import com.heetch.countrypicker.CountryPickerDialog;
import com.texaconnect.texa.R;
import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.databinding.FragmentLoginBinding;
import com.texaconnect.texa.databinding.FragmentProfileBinding;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;


import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    AutoClearedValue<FragmentProfileBinding> binding;
    private UserViewModel userViewModel;
    private  CountryPickerDialog mCountryPicker;
    private String mCountryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,
                container, false, dataBindingComponent);


//        dataBinding.setCallback(loginCallBacks);
        binding = new AutoClearedValue<>(this, dataBinding);

        mCountryPicker =
                new CountryPickerDialog(getContext(), new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        mCountryName = country.getIsoCode();
                        Locale locale = new Locale("",mCountryName);
                        binding.get().location.setText(locale.getDisplayCountry());
                        Log.d("COUNTRY : ", "onCountrySelected: "+mCountryName);
                    }
                });

        dataBinding.location.setOnClickListener(this);
        dataBinding.buttonSave.setOnClickListener(this);
        dataBinding.profileCloseBtn.setOnClickListener(this);




        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.observeProfileData().observe(this,response->{
            Log.d("LOGIN RESPONSE : ", "onActivityCreated: "+response.status);

            if (response.status == Status.SUCCESS){
                mListener.cancelProgressDialog();
                binding.get().email.setText(response.data.email);
                binding.get().fname.setText(response.data.name);
                if (response.data.phone!=null){
                    binding.get().phone.setText(response.data.phone);
                }
                mCountryName = response.data.locationCode;
                Locale locale = new Locale("",response.data.locationCode);
                binding.get().location.setText(locale.getDisplayCountry());
//                binding.get().password.setText(TexaApplication.sPassword);
            }else if(response.status==Status.LOADING){
                mListener.showProgressDialog("Gathering info");
            }
            else {
                mListener.cancelProgressDialog();
                Toast.makeText(getActivity(), ""+response.message, Toast.LENGTH_SHORT).show();

            }
        });
        userViewModel.getProfileData();

        // for posting user profile details
        userViewModel.UpdateProfileData().observe(this, response->{
            if (response.status == Status.SUCCESS){
                mListener.cancelProgressDialog();
                mListener.showToast("Updated details");
            }else if (response.status == Status.LOADING){
                mListener.showProgressDialog("Uploading details");
            }else {
                mListener.cancelProgressDialog();
                Toast.makeText(getActivity(), ""+response.message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.location:
                mCountryPicker.show();
                break;
            case R.id.buttonSave:

                userViewModel.updateProfile(
                        binding.get().fname.getText().toString().trim(),
                        binding.get().phone.getText().toString().trim(),
                        mCountryName.toLowerCase().trim()
                );
                break;
            case R.id.profile_close_btn:
                Objects.requireNonNull(getActivity()).finish();
                break;
        }
    }
}
