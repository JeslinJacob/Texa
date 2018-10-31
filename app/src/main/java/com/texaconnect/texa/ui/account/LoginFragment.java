package com.texaconnect.texa.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.texaconnect.texa.R;
import com.texaconnect.texa.TexaApplication;
import com.texaconnect.texa.databinding.FragmentLoginBinding;
import com.texaconnect.texa.interfaces.LoginCallBacks;
import com.texaconnect.texa.model.Status;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.ui.MainActivity;
import com.texaconnect.texa.util.AutoClearedValue;
import com.texaconnect.texa.util.MyUtils;

public class LoginFragment extends BaseFragment {

    AutoClearedValue<FragmentLoginBinding> binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentLoginBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,
                container, false, dataBindingComponent);

        dataBinding.setCallback(loginCallBacks);
        binding = new AutoClearedValue<>(this, dataBinding);
        binding.get().termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*FragmentManager fragmentManager = getFragmentManager();
                ScanWifiFragment wifiFragment = ScanWifiFragment.newInstance("title");
                wifiFragment.show(fragmentManager,"lang_dialog");
                wifiFragment.setCancelable(false);*/
            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getLoginData().observe(this, response -> {

            Log.d("LOGIN RESPONSE : ", "onActivityCreated: "+response.status);
            // update UI
            if (response.status == Status.SUCCESS) {

                Log.d("LoginFragment","Access token : "+response.data.getAccess_token()+"refresh tocken : "+response.data.getRefresh_token());
                mListener.cancelProgressDialog();
                TexaApplication.getApp().saveTokens(response.data.getAccess_token(),response.data.getRefresh_token());
                TexaApplication.getApp().setLoggedIn(true, response.data);

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, response.data.getName());
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ""+response.data.getId());
                FirebaseAnalytics.getInstance(getContext()).logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

//                EventBus.getDefault().post(new LoginEvent());
                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }else if(response.status==Status.LOADING){
                mListener.showProgressDialog("Logging in");
            }
            else {
                mListener.cancelProgressDialog();

                Toast.makeText(getActivity(), ""+response.message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    LoginCallBacks loginCallBacks = new LoginCallBacks() {
        @Override
        public void onForgotPasswordClick(View view) {
//            Fragment fragment = Fragment.instantiate(getActivity(), ForgetPasswordFragment.class.getName());
//            assert getFragmentManager() != null;
//            getFragmentManager().beginTransaction()
//                    .addToBackStack(null)
//                    .add(R.id.container, fragment)
//                    .commit();
            navigationController.navigateToForgotPassword();

        }

        @Override
        public void onSignInClick(View view) {
            boolean email, password;
            String emailAddress = binding.get().email.getText().toString();
            String passwordString = binding.get().password.getText().toString();
            if (!TextUtils.isEmpty(emailAddress) && MyUtils.isValidEmail(emailAddress)) {
                email = true;
            } else {
                email = false;

//                binding.get().email.setError("Invalid Email");

                binding.get().inputLayoutEmail.setError("Invalid Email");
//                Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
            }

            if (!TextUtils.isEmpty(passwordString) && passwordString.length() > 0) {
                password = true;
            } else {
                password = false;
//                Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_SHORT).show();
//                binding.get().password.setError("Invalid Password");
                binding.get().inputLayoutPassword.setError("Invalid Password");
            }

            if (email && password) {

                TexaApplication.getApp().saveUserCredentials(emailAddress, passwordString);
                userViewModel.login("password", emailAddress, passwordString);

//                Intent intent = new Intent(getContext(),MainActivity.class);
//                startActivity(intent);
            }

        }

        @Override
        public void onSignUpClick(View view) {
            navigationController.navigateToSignUp();

        }
    };

/*
    public void login(String emailAddress, String passwordString){
        TexaApplication.getApp().getmApiInterface().login(new LoginRequest(emailAddress,
                passwordString)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }
*/
}
