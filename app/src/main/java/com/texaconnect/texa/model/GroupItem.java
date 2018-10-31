package com.texaconnect.texa.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupItem {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("elements")
    @Expose
    public List<Element> elements = null;
    public int deviceStatus;

}
