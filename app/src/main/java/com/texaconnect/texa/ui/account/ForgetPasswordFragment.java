package com.texaconnect.texa.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentForgetpasswordBinding;
import com.texaconnect.texa.interfaces.CallBacks;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;
import com.texaconnect.texa.util.MyUtils;
import com.texaconnect.texa.model.Status;

public class ForgetPasswordFragment extends BaseFragment {

    AutoClearedValue<FragmentForgetpasswordBinding> binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentForgetpasswordBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgetpassword,
                container, false, dataBindingComponent);

        dataBinding.setCallback(callBacks);
        binding = new AutoClearedValue<>(this, dataBinding);
        binding.get().closeForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.closeActionRegistration();
            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getForgotPasswordData().observe(this, response -> {
            // update UI
            if (response.status == Status.SUCCESS) {

                mListener.cancelProgressDialog();
                mListener.showToast("Please check your email ");
                Log.d("NEW PASSWORD", "onActivityCreated: "+response.data);
//                FragmentManager fragmentManager = getFragmentManager();
//                assert fragmentManager != null;
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                Fragment fragment = Fragment.instantiate(getActivity(), LoginFragment.class.getName());
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .commitAllowingStateLoss();
                navigationController.navigateToLoginFragment();
            } else if(response.status==Status.LOADING){
                mListener.showProgressDialog("Sending details to your email");
            }
            else{
                mListener.cancelProgressDialog();
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    CallBacks callBacks = new CallBacks() {
        @Override
        public void resetClick(View view) {
            boolean email;
            String emailAddress = binding.get().email.getText().toString();
            if (!TextUtils.isEmpty(emailAddress) && MyUtils.isValidEmail(emailAddress)) {
                email = true;
            } else {
                email = false;
//                binding.get().email.setError("Invalid Email");
                binding.get().inputLayoutEmail.setError("Invalid Email");
            }

            if (email) {
                userViewModel.forgotPassword(emailAddress);
            }
        }

        @Override
        public void closeClick(View view) {
            navigationController.closeActionRegistration();
        }
    };



}