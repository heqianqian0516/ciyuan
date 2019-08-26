package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class AppVersionItem implements Serializable {
    @SerializedName("appType")
    public int appType;//应用类型-安卓/ios(10:IOS,20:安卓)
    @SerializedName("channelCode")
    public String channelCode;// 渠道编号
    @SerializedName("contentText")
    public String contentText;//详细信息
    @SerializedName("isForce")
    public int isForce;//是否强制更新
    @SerializedName("title")
    public String title;//标题
    @SerializedName("url")
    public String url;// 下载链接
    @SerializedName("version")
    public String version;//版本号
    @SerializedName("versionCode")
    public int versionCode;//版本号
    @SerializedName("lastForceVersionCode")
    public int lastForceVersionCode;//
}
