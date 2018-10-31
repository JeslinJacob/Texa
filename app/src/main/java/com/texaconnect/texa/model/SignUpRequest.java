package com.texaconnect.texa.model;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("name")
    public String name;

    @SerializedName("phone")
    public String phone;

    @SerializedName("locationCode")
    public String  locationCode;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public  String longitude;

    @SerializedName("timeZone")
    public  String timeZone;

    public SignUpRequest(String email, String password, String name,String phone,
                         String locationCode, String latitude, String longitude,
                         String timeZone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.locationCode = locationCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = timeZone;
    }

//    public SignUpRequest(String email, String password, String firstName, String lastName) {
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
}
