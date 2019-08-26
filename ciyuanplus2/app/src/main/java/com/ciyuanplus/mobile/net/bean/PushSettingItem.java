package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class PushSettingItem implements Serializable {
    @SerializedName("chatmessagePush")
    public int chatmessagePush;//聊天提醒 ,
    @SerializedName("commentPush")
    public int commentPush;//评论  ,
    @SerializedName("deviceType")
    public int deviceType;//设备类型  ,
    @SerializedName("followerPush")
    public int followerPush;//新粉丝 ,
    @SerializedName("systemmessagePush")
    public int systemmessagePush;// ,
    @SerializedName("deviceToken")
    public String deviceToken;//
    @SerializedName("id")
    public int id;//,
}
