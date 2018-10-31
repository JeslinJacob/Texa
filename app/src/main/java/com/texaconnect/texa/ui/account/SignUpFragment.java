package com.texaconnect.texa.ui.account;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentSignupBinding;
import android.location.Address;

import com.texaconnect.texa.interfaces.PermissionCallback;
import com.texaconnect.texa.model.SignUpRequest;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;
import com.texaconnect.texa.util.MyUtils;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.util.PermissionUtils;

import java.util.List;
import java.util.Locale;

public class SignUpFragment extends BaseFragment {

    AutoClearedValue<FragmentSignupBinding> binding;
    FusedLocationProviderClient mFusedLocationClient;
    String mEmailString,mCountryCode = "IN";
    Double mLat,mLng;
//            countryCodeValue;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
//        AndroidSupportInjection.inject(this);
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSignupBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup,
                container, false, dataBindingComponent);
        String[] location_perm = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };
        mListener.onRequestPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {

                getLastKnownLocation();
            }

            @Override
            public void onFailed() {
                mListener.showToast("Some features may not work");
            }
        },location_perm);

        binding = new AutoClearedValue<>(this, dataBinding);


        dataBinding.signup.setOnClickListener(v -> {
            signUp();
        });

        dataBinding.closeRegister.setOnClickListener(view -> {
            closeReg();
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getSignUpData().observe(this, response -> {
            // update UI
            if (response.status == Status.SUCCESS) {

                mListener.cancelProgressDialog();
                mListener.showToast("Account has been created");
                Log.d("EMAIL : ", "onActivityCreated: "+mEmailString);
                navigationController.navigateToFinishPage(mEmailString);


            } else if(response.status==Status.LOADING){
                mListener.showProgressDialog("Signing up");

            }  else {

                Log.d("SIGN UP ISSUE", ": "+response+" and "+response.message);
                mListener.cancelProgressDialog();
                mListener.showToast(response.message);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getLastKnownLocation() {

        Log.d("FINE LOCATION", "getLastKnownLocation ACCESS_FINE_LOCATION " + PermissionUtils.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
        Log.d("COARSE LOCATION", "getLastKnownLocation ACCESS_COARSE_LOCATION " + PermissionUtils.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    // In some rare cases the location returned can be null
                    Log.d("LOCATION NULL ", "getLastLocation null " + (location == null));
                    if (location == null) {
                        mListener.showToast(getString(R.string.no_location_available));
                    }else {

                        mLat = location.getLatitude();
                        mLng=location.getLongitude();
//                        mListener.showToast(" LOCATION : lat"+location.getLatitude()+" longitude: "+location.getLongitude());
                        getCountryCode();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FAILED COUNTRY LOAD", "onFailure: "+e.getMessage());
            }
        });
    }



    public void closeReg(){
        navigationController.closeActionRegistration();
    }

    public void signUp() {


        boolean fname, phone, email, password;
        binding.get().inputLayoutPassword.setErrorEnabled(false);
        binding.get().inputLayoutEmail.setErrorEnabled(false);
        binding.get().inputLayoutName.setErrorEnabled(false);

        String fnameString = binding.get().fname.getText().toString().trim();
        String phoneString = "";
        mEmailString = binding.get().email.getText().toString().trim();
        String passwordString = binding.get().password.getText().toString().trim();

        if (!TextUtils.isEmpty(fnameString) && fnameString.length() > 3) {
            fname = true;
        } else {
            fname = false;
            binding.get().inputLayoutName.setError("invalid name");
       }


        if (!TextUtils.isEmpty(mEmailString) && MyUtils.isValidEmail(mEmailString)) {
            email = true;
        } else {
            email = false;
//            binding.get().email.setError("Invalid  email");
            binding.get().inputLayoutEmail.setError("Invalid email");
//            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
        }

        if (!TextUtils.isEmpty(passwordString) && MyUtils.isValidPassword(passwordString)) {
            password = true;
        } else {
            password = false;
//            binding.get().password.setError("Invalid password");
            binding.get().inputLayoutPassword.setError("Invalid password");
//            Toast.makeText(getActivity(), "Password Invalid", Toast.LENGTH_SHORT).show();
        }

        if (fname && email && password) {
         SignUpRequest signUpRequest = new SignUpRequest(mEmailString,passwordString,fnameString,phoneString,mCountryCode,"111","111","IST");
            userViewModel.signUp(signUpRequest);
        }

    }

    private void getCountryCode(){
        String countryName="";
        try {

            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(mLat, mLng, 1);

            if (addresses.size() > 0)
            {
                countryName=addresses.get(0).getCountryName();
                mCountryCode =addresses.get(0).getCountryCode();
                Log.d("COUNTRY Code", ": "+mCountryCode);
            }
        }catch (Exception e){
        Log.e("LOCATION EXCEPTION", "signUp: "+e.getMessage() );
        }
        binding.get().location.setText(countryName);

    }
}