package com.texaconnect.texa.ui.account;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texaconnect.texa.R;
import com.texaconnect.texa.databinding.FragmentFinishRegistrationBinding;
import com.texaconnect.texa.ui.BaseFragment;
import com.texaconnect.texa.util.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishRegistrationFragment extends BaseFragment {

    AutoClearedValue<FragmentFinishRegistrationBinding> binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        FragmentFinishRegistrationBinding dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_finish_registration,
                container,false,dataBindingComponent);
//        binding = new AutoClearedValue<>(this, dataBinding);

        dataBinding.confEmailTxt.setText(
                Html.fromHtml(getString(R.string.email_conf_msg)+"<font color='#199CDB'>"+
                        " "+bundle.getString("email")+"</font>"));

        dataBinding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.navigateToLoginFragment();
            }
        });
        // Inflate the layout for this fragment
        return dataBinding.getRoot();
    }

}
