package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class CommunityDetailItem implements Serializable {
    @SerializedName("addressUuid")
    public String addressUuid;//id
    @SerializedName("address")
    public String address;//详细地址

}
