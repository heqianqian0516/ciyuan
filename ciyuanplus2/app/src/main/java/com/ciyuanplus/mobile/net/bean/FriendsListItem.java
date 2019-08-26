package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class FriendsListItem implements Serializable {
    @SerializedName("list")
    public FriendsItem[] list;//小区名称
}
