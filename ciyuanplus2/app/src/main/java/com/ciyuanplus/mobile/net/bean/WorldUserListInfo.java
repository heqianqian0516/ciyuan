package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/5/5.
 */

public class WorldUserListInfo implements Serializable {
    @SerializedName("list")
    public WorldUserItem[] list;//

}
