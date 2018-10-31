package com.texaconnect.texa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    transient private static final String TAG = "User";
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;
    @SerializedName("expires_in")
    @Expose
    public Integer expiresIn;
    @SerializedName("scope")
    @Expose
    public String scope;
    @SerializedName("user")
    @Expose
    public UserData userData;

    public boolean isSignedIn() {
        return userData.id > 0;
    }


    public String getAccess_token() {
        return accessToken;
    }

    public String getRefresh_token() {
        return refreshToken;
    }

    public int getId() {
        return userData.id;
    }

    public void setId(int id) {
        this.userData.id = id;
    }

//    public void setAddress(String address) {
//        this.address = address;
//    }

//    public String getAddress() {
//        return address;
//    }

    public String getEmail() {
        return userData.email;
    }


//    public String getImage() {
//        return image;
//    }

//    public void setImage(String image) {
//        this.image = image;
//    }


//    public String getLname() {
//        return lname;
//    }


    public String getLocationCode() {
        return userData.locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.userData.locationCode = locationCode;
    }

    public String getPhone() {
        return userData.phone;
    }

    public void setEmail(String email) {
        this.userData.email = email;
    }

    public String getName() {
        return userData.name;
    }

    public void setFname(String name) {
        this.userData.name = name;
    }

//    public void setLname(String lname) {
//        this.lname = lname;
//    }

    public void setPhone(String phone) {
        this.userData.phone = phone;
    }


    @Override
    public String toString() {
        return "User{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", scope='" + scope + '\'' +
                ", userData=" + userData +
                '}';
    }
}
