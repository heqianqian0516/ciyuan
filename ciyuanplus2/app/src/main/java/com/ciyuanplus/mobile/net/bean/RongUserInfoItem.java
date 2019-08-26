package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class RongUserInfoItem implements Serializable {

    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;//
    @SerializedName("identity")
    public int identity;//
    @SerializedName("isPublish")
    public int isPublish;//
    @SerializedName("nickname")
    public String nickname;//
    @SerializedName("sex")
    public int sex;//
    @SerializedName("photo")
    public String photo;//
    @SerializedName("uuid")
    public String uuid;//
}
