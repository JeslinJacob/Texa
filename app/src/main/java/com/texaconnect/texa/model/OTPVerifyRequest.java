package com.texaconnect.texa.model;

import com.google.gson.annotations.SerializedName;

public class OTPVerifyRequest {

    @SerializedName("otp")
    String otp;

    @SerializedName("email")
    String email;

    public OTPVerifyRequest(String otp, String email) {
        this.otp = otp;
        this.email = email;
    }
}
