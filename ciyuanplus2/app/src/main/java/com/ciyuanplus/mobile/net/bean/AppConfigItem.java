package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/6/9.
 */

public class AppConfigItem implements Serializable {
    @SerializedName("mobilePortalUrl")
    public String mobilePortalUrl;//手机门户地址
    @SerializedName("pcPortalUrl")
    public String pcPortalUrl;//PC门户地址
    @SerializedName("shareLinkContent")
    public String shareLinkContent;//分享内容
    @SerializedName("shareLinkTitle")
    public String shareLinkTitle;//分享标题
    @SerializedName("flashPicImage")
    public String flashPicImage;// 闪屏图片
    @SerializedName("flashPicTimeout")
    public final int flashPicTimeout = 3;//闪屏出现秒数  默认是3
    @SerializedName("postTypeDict")
    public PostTypeItem[] postTypeDict;//帖子标签字典
    @SerializedName("wikiTypeDict")
    public WikiTypeItem[] wikiTypeDict;//Wiki类型字典
    @SerializedName("flashPicUrl")
    @Nullable
    public String flashPicUrl;//闪屏链接，有责有没有则没有 ,
}
