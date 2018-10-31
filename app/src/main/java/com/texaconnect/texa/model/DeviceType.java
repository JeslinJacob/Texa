
package com.texaconnect.texa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceType {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("typeId")
    @Expose
    public String typeId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;

}