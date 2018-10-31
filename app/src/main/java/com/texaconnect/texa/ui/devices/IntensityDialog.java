package com.texaconnect.texa.ui.devices;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.texaconnect.texa.R;
import com.texaconnect.texa.binding.FragmentDataBindingComponent;
import com.texaconnect.texa.databinding.FragmentIntensityDialogBinding;
import com.texaconnect.texa.event.MQTTSendMessageEvent;
import com.texaconnect.texa.model.Node;
import com.texaconnect.texa.util.AutoClearedValue;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntensityDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntensityDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "IntensityDialog";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int STEP_SIZE = 20;

    // TODO: Rename and change types of parameters
    private String mTopic;
    AutoClearedValue<FragmentIntensityDialogBinding> binding;
    protected DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;
    private MqttViewModel mqttViewModel;
    private Node mNode;

    public IntensityDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param topic Parameter 1.
     * @return A new instance of fragment IntensityDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static IntensityDialog newInstance(String topic) {
        IntensityDialog fragment = new IntensityDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopic = getArguments().getString(ARG_PARAM1);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentIntensityDialogBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intensity_dialog,
                container, false, dataBindingComponent);

//        dataBinding.seekbar.setProgress(Integer.parseInt(mDeviceStatus.substring(1)));

        binding = new AutoClearedValue<>(this, dataBinding);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mqttViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MqttViewModel.class);

        mNode = mqttViewModel.getNodeByTopic(mTopic);
        Log.d(TAG, "onActivityCreated topic " + mTopic);
        Log.d(TAG, "onActivityCreated " + mNode.toString());
        binding.get().setNode(mNode);
        binding.get().executePendingBindings();

        binding.get().seekbar.setOnSeekBarChangeListener(this);
        binding.get().switch1.setOnCheckedChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            progress = progress / STEP_SIZE;
            progress = progress * STEP_SIZE;

            seekBar.setProgress(progress);
            String message = "" + mNode.deviceStatus.charAt(0);
            if (progress == 100) {
                progress = 99;
            }
            switch (mNode.deviceStatus.charAt(0)) {
                case '0':
                case '1':
                    message = (progress == 0) ? "000" : "1" + progress;
                    break;
                case '2':
                case '3':
                    message = (progress == 0) ? "200" : "3" + progress;
                    break;
            }
            EventBus.getDefault().post(new MQTTSendMessageEvent(mNode.sendMessageTopic, message));

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String message = "000";
        switch (mNode.deviceStatus.charAt(0)) {
            case '0':
            case '1':
                message = (isChecked ? "1" : "0") + mNode.getIntensity();
                break;
            case '2':
            case '3':
                message = (isChecked ? "3" : "2") + mNode.getIntensity();
                break;
        }
        EventBus.getDefault().post(new MQTTSendMessageEvent(mNode.sendMessageTopic, message));

    }
}
