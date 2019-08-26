package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class FeedBackItem implements Serializable {
    @SerializedName("contentText")
    public String contentText;//: 内容
    @SerializedName("createTime")
    public String createTime;//: 时间
    @SerializedName("sendUserUuid")
    public String sendUserUuid;//:发送人,空代表客服回复 ,
    @SerializedName("uuid")
    public String uuid;//:
    @SerializedName("messageType")
    public int messageType;//类型1文字2图片 ,
}
