package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActivityListItem implements Serializable {
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("imgs")
    public String imgs;
    @SerializedName("activityName")
    public String activityName;
    @SerializedName("activityContent")
    public String activityContent;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("isDel")
    public String isDel;
    @SerializedName("isPublish")
    public String isPublish;


}
