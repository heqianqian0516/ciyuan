package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//参加活动bean
public class ActivityItem implements Serializable {
    @SerializedName("list")
    public ActivityListItem[] list;
    @SerializedName("isPublic")
    public String isPublic;
    @SerializedName("modifyTime")
    public String modifyTime;


}
