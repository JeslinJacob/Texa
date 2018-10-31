package com.texaconnect.texa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("timeZone")
    @Expose
    public String timeZone;
    @SerializedName("locationCode")
    @Expose
    public String locationCode;
    @SerializedName("postalCode")
    @Expose
    public Object postalCode;
    @SerializedName("latitude")
    @Expose
    public Integer latitude;
    @SerializedName("longitude")
    @Expose
    public Integer longitude;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("enabled")
    @Expose
    public Boolean enabled;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("authorities")
    @Expose
    public List<Authority> authorities = null;
    @SerializedName("accountNonExpired")
    @Expose
    public Boolean accountNonExpired;
    @SerializedName("accountNonLocked")
    @Expose
    public Boolean accountNonLocked;
    @SerializedName("credentialsNonExpired")
    @Expose
    public Boolean credentialsNonExpired;

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", postalCode=" + postalCode +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", phone='" + phone + '\'' +
                '}';
    }
}
