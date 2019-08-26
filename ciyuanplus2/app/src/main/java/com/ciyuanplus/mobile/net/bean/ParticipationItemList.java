package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParticipationItemList implements Serializable {
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("contentImg")
    public String contentImg;
    @SerializedName("imgs")
    public String imgs;
    @SerializedName("activityName")
    public String activityName;
    @SerializedName("activityContent")
    public String activityContent;

}
