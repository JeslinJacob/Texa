package com.texaconnect.texa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("device")
    @Expose
    public Device device;
    @SerializedName("node")
    @Expose
    public Node node;

}
