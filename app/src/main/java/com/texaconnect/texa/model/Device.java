
package com.texaconnect.texa.model;

import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.texaconnect.texa.BR;

import java.util.List;

public class Device extends MQTTModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("serial")
    @Expose
    public String serial;
    @SerializedName("hwAddress")
    @Expose
    public String hwAddress;
    @SerializedName("locationCode")
    @Expose
    public String locationCode;
    @SerializedName("deviceType")
    @Expose
    public DeviceType deviceType;
    @SerializedName("nodes")
    @Expose
    public List<Node> nodes = null;

    public String subscriptionTopic;
    @Bindable
    public int deviceStatus;
    public boolean isSubscribed;
    public State state = State.OFFLINE;

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = Integer.parseInt(deviceStatus);
        switch (deviceStatus.charAt(0)) {
            case '0':
                state = State.OFF;
                break;
            case '1':
                state = State.ON;
                break;
            default:
                state = State.OFFLINE;
                break;
        }
        notifyPropertyChanged(BR.deviceStatus);
    }

}
