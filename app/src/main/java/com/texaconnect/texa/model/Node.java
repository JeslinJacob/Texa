package com.texaconnect.texa.model;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.texaconnect.texa.BR;
import com.texaconnect.texa.R;
import com.texaconnect.texa.event.MQTTSendMessageEvent;

import org.greenrobot.eventbus.EventBus;

public class Node extends MQTTModel {

    public enum MODE {
        SWITCH,
        DIMMER
    }

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("deviceId")
    @Expose
    public Integer deviceId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("nodeId")
    @Expose
    public String nodeId;
    @SerializedName("iconName")
    @Expose
    public String iconName;
    @SerializedName("autoOffMinutes")
    @Expose
    public Integer autoOffMinutes;
    @SerializedName("switchOnNotification")
    @Expose
    public Boolean switchOnNotification;
    @SerializedName("switchOffNotification")
    @Expose
    public Boolean switchOffNotification;
    @SerializedName("mode")
    @Expose
    public MODE mode;
    @SerializedName("supportedModes")
    @Expose
    public String supportedModes;

    public String subscriptionTopic;
    public String sendMessageTopic;
    public String deviceStatus;
    @Bindable
    public int drawable = R.drawable.ic_power_settings_new_gray_24dp;;
    public State state = State.OFFLINE;
    public boolean isSubscribed;
    public boolean isEnabled;

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
        isEnabled = true;

        switch (deviceStatus.charAt(0)) {
            case '0':
                state = State.OFF;
                drawable = R.drawable.ic_power_settings_new_red_24dp;
                break;
            case '1':
                state = State.ON;
                drawable = R.drawable.ic_power_settings_new_blue_24dp;
                break;
            case '2':
                state = State.OFFLOCK;
                drawable = R.drawable.ic_power_settings_new_red_24dp;
                break;
            case '3':
                state = State.ONLOCK;
                drawable = R.drawable.ic_power_settings_new_blue_24dp;
                break;
            default:
                state = State.OFFLINE;
                isEnabled = false;
                drawable = R.drawable.ic_power_settings_new_gray_24dp;
                break;
        }
        notifyPropertyChanged(BR.drawable);
        notifyPropertyChanged(BR.isLocked);
        notifyPropertyChanged(BR.intensity);
        notifyPropertyChanged(BR.switchedOn);
    }

    @Bindable
    public boolean getIsLocked() {
        if (!TextUtils.isEmpty(deviceStatus)) {
            return deviceStatus.charAt(0) == '2' || deviceStatus.charAt(0) == '3';
        }
        return false;

    }

    @Bindable
    public int getIntensity(){
        return Integer.parseInt(mode == MODE.DIMMER ? deviceStatus.substring(1) : "0");
    }

    @Bindable
    public boolean isSwitchedOn(){

        return deviceStatus.charAt(0) == '1';
    }
    public boolean isAvailable(){
        return isEnabled && state != State.OFFLINE;
    }

    public boolean isDimmer(){
        return mode == MODE.DIMMER;
    }

    public void toggleSwitch() {

//        int deviceState = 000;
        String message = "099";
        if (isEnabled && state != State.OFFLINE) {
            switch (state) {
                case OFF :
//                    deviceState = 1;
                    message = "199";
                    break;
                case ON:
//                    deviceState = 0;
                    message = "099";
                    break;
                case OFFLOCK:
//                    deviceState = 3;
                    message = "399";
                    break;
                case ONLOCK:
//                    deviceState = 2;
                    message = "299";
                    break;
            }
//            message = "" + deviceState + deviceStatus.substring(1);
            EventBus.getDefault().post(new MQTTSendMessageEvent(sendMessageTopic, message));
            isEnabled = false;
        }

    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", name='" + name + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", mode=" + mode +
                ", supportedModes='" + supportedModes + '\'' +
                ", subscriptionTopic='" + subscriptionTopic + '\'' +
                ", sendMessageTopic='" + sendMessageTopic + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", state=" + state +
                ", isSubscribed=" + isSubscribed +
                ", isEnabled=" + isEnabled +
                '}';
    }
}