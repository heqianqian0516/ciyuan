package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class ReplyItem implements Serializable {
    @SerializedName("commentUuid")
    public String commentUuid;//评论uuid , ,
    @SerializedName("contentText")
    public String contentText;//评论内容 ,
    @SerializedName("createTime")
    public String createTime;//创建时间 ,
    @SerializedName("parentCommentUuid")
    public String parentCommentUuid;//评论父级uuid , ,
    @SerializedName("sendNickname")
    public String sendNickname;//发送用户昵称 , ,
    @SerializedName("sendUserUuid")
    public String sendUserUuid;// 发送用户uuid , ,
    @SerializedName("toNickname")
    public String toNickname;//新鲜事发布用户头像
    @SerializedName("toUserUuid")
    public String toUserUuid;//新鲜事时间 ,
    public boolean isAnyom = false; // 是否需要匿名
    @SerializedName("sendPhoto")
    public String sendPhoto;//发送用户头像 ,
    @SerializedName("isAnonymous")
    public int isAnonymous; // 是否需要匿名
}
