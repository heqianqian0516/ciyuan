package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class BannerItem implements Serializable {
    @SerializedName("id")
    public String id;// ,
    @SerializedName("type")
    public int type;//
    @SerializedName("img")
    public String img;// ,
    @SerializedName("name")
    public String name;// ,
    @SerializedName("param")
    public String param;// ,
    @SerializedName("orderNum")
    public int orderNum;//
    @SerializedName("updateTime")
    public String updateTime;// ,
    @SerializedName("bizUuid")
    public String bizUuid;// ,
    @SerializedName("allowedUserState")
    public int allowedUserState;// ,
}
