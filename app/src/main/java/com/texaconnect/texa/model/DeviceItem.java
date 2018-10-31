
package com.texaconnect.texa.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.texaconnect.texa.BR;

public class DeviceItem  {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("ownerType")
    @Expose
    public String ownerType;
    @SerializedName("device")
    @Expose
    public Device device;
}
