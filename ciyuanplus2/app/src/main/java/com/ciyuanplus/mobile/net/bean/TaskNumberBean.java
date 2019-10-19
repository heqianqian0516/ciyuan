package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskNumberBean implements Serializable {
    @SerializedName("postNum")
    public int cpostNum;
    @SerializedName("commentNum")
    public int commentNum;
    @SerializedName("signNum")
    public int signNum;
    @SerializedName("shopNum")
    public int shopNum;
    @SerializedName("shareNum")
    public int shareNum;
    @SerializedName("likeNum")
    public int likeNum;
}
