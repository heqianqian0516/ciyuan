package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RankedFirstItem implements Serializable {
    @SerializedName("activityUuid")
    public String activityUuid;
    @SerializedName("postUuid")
    public String postUuid;
    @SerializedName("photo")
    public String photo;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("rank")
    public int rank;
    @SerializedName("type")
    public int type = 1;
    @SerializedName("imgs")
    public String imgs;
    @SerializedName("bizType")
    public String bizType;
}
