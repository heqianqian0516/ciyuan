package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2018/1/9.
 */

public class RateItem {
    @SerializedName("contentText")
    public String contentText;// 评论内容 , ,
    @SerializedName("createTime")
    public String createTime;// ,评论创建时间 , ,
    @SerializedName("nickname")
    public String nickname;// ,用户昵称 , ,
    @SerializedName("photo")
    public String photo;// ,用户头像 , ,
    @SerializedName("score")
    public double score;// ,评论分数 ,
    @SerializedName("sex")
    public int sex;// 用户性别
    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;// ,当前小区id , ,
    @SerializedName("userUuid")
    public String userUuid;// 用户id
}
