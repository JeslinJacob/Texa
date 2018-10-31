package com.texaconnect.texa.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("user_email")
    public String email;

    @SerializedName("user_password")
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
}
