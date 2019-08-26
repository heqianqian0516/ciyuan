package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class CommunityListItem implements Serializable {
    @SerializedName("data")
    public CommunityItem[] data;//小区名称
}
