package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewTaskBean implements Serializable {
    @SerializedName("nickname")
    public String data;
    @SerializedName("photo")
    public String photo;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("address")
    public String address;
    @SerializedName("postNum")
    public int postNum;


}
