package com.texaconnect.texa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateRequest {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("locationCode")
    @Expose
    public String locationCode;

    public ProfileUpdateRequest(String name,String mobile,String location) {
        this.name = name;
        this.locationCode=location;
        this.phone = mobile;
    }
}
