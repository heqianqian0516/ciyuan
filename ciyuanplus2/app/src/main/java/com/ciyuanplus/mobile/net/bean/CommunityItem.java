package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class CommunityItem implements Serializable {
    @SerializedName("commName")
    public String commName;//小区名称
    @SerializedName("uuid")
    public String uuid;//小区uuid
    @SerializedName("isAddress")
    public int isAddress;//是否有详细地址
    @SerializedName("userCommunityAddressResults")
    public CommunityDetailItem[] userCommunityAddressResults;//小区详细地址
    public boolean isClosed = false;// 是否是关闭的， 默认是false

}
