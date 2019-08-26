package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class NoticeItem implements Serializable {
    @SerializedName("bizUuid")
    public String bizUuid;//业务数据UUID ,
    @SerializedName("contentText")
    public String contentText;//消息内容 ,
    @SerializedName("createTime")
    public String createTime;//发送时间 ,
    @SerializedName("fromUserUuid")
    public String fromUserUuid;//发送人UUID,为空时代表系统发送 ,
    @SerializedName("isRead")
    public int isRead;//是否已读 , , ,
    @SerializedName("nickname")
    public String nickname;// 发送人昵称 ,
    @SerializedName("noticeType")
    public int noticeType;//消息类型 0系统1评论2喜欢3踩4关注 ,5回复
    @SerializedName("photo")
    public String photo;//发送人头像 ,
    @SerializedName("uuid")
    public String uuid;// ,
    @SerializedName("sex")
    public int sex;// ,
    @SerializedName("neibType")
    public int neibType;//邻居类型 0-无|1-邻居 ,
    @SerializedName("bizType")
    public int bizType;//业务类型(0:帖子，1:宝贝)
    @SerializedName("renderType")
    public int renderType;//浏览量
    @SerializedName("subBizUuid")
    private String subBizUuid;//  ,
    @SerializedName("currentBizUuid")
    public String currentBizUuid;//  ,
    @SerializedName("isAnonymous")
    public int isAnonymous; // 是否需要匿名

    @Override
    public String toString() {
        return "NoticeItem{" +
                "bizUuid='" + bizUuid + '\'' +
                ", contentText='" + contentText + '\'' +
                ", createTime='" + createTime + '\'' +
                ", fromUserUuid='" + fromUserUuid + '\'' +
                ", isRead=" + isRead +
                ", nickname='" + nickname + '\'' +
                ", noticeType=" + noticeType +
                ", photo='" + photo + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sex=" + sex +
                ", neibType=" + neibType +
                ", bizType=" + bizType +
                ", renderType=" + renderType +
                ", subBizUuid='" + subBizUuid + '\'' +
                ", currentBizUuid='" + currentBizUuid + '\'' +
                ", isAnonymous=" + isAnonymous +
                '}';
    }
}
