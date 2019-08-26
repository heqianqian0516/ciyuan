package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RankingListBean implements Serializable {
    @SerializedName("list")
    public RankListItem[] list;

}
