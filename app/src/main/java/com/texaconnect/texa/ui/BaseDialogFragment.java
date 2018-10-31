package com.texaconnect.texa.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.texaconnect.texa.binding.FragmentDataBindingComponent;
import com.texaconnect.texa.di.Injectable;
import com.texaconnect.texa.interfaces.OnFragmentInteractionListener;
import com.texaconnect.texa.ui.common.NavigationController;

import javax.inject.Inject;

public class BaseDialogFragment extends DialogFragment implements Injectable {

    protected OnFragmentInteractionListener mListener;
    //    @Inject
    protected NavigationController navigationController;
    protected DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onAttach(Context context) {
//        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        navigationController = new NavigationController(getActivity());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}