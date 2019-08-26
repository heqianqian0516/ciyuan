package com.ciyuanplus.mobile.net.bean;

import com.ciyuanplus.mobile.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/5/5.
 */

public class UserInfoItem implements Serializable {
    public static final int SEX_NONE = 0;
    public static final int SEX_MAN = 1;
    public static final int SEX_FEMALE = 2;

    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;// 用户当前所在小区 ,
    @SerializedName("currentCommunityName")
    public String currentCommunityName;// 用户当前所在小区 ,
    @SerializedName("identity")
    public int identity;// 用户身份 ,
    @SerializedName("isPublish")
    public int isPublish;// 冻结状态 ,
    @SerializedName("mobile")
    public String mobile;// 用户手机号 ,
    @SerializedName("nickname")
    public String nickname;// 用户昵称 ,
    @SerializedName("photo")
    public String photo = ""; // 用户头像 ,
    @SerializedName("recoCode")
    public String recoCode;// 用户邀请码 ,
    @SerializedName("sex")
    public int sex;
    @SerializedName("state")
    public int state;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("token")
    public String token;
    @SerializedName("isFollow")
    public int isFollow;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("isPassword")
    public int isPassword;
    @SerializedName("personalizedSignature")
    public String personalizedSignature;
    @SerializedName("activityUuid")
    public String activityUuid;
    @SerializedName("userUuid")
    public String userUuid;
    // 获取性别图片
    public static int getSexImageResource(int ss) {
        if (ss == UserInfoItem.SEX_NONE) return 0;
        return ss == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy_small : R.mipmap.my_icon_girl_small;
    }

    // 获取性别图片
    public static int getBigSexImageResource(int ss) {
        if (ss == UserInfoItem.SEX_NONE) return 0;
        return ss == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy : R.mipmap.my_icon_girl;
    }

    public String getUserSex() {
        String sSexText = "未知";
        if (SEX_MAN == sex) sSexText = "男";
        else if (SEX_FEMALE == sex) sSexText = "女";
        return sSexText;
    }

    // 获取性别图片
    public int getSexImageResource() {
        if (sex == UserInfoItem.SEX_NONE) return 0;
        return sex == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy_small : R.mipmap.my_icon_girl_small;
    }

    // 获取性别图片
    public int getBigSexImageResource() {
        if (sex == UserInfoItem.SEX_NONE) return 0;
        return sex == UserInfoItem.SEX_MAN ?
                R.mipmap.my_icon_boy : R.mipmap.my_icon_girl;
    }

    public String getUserState() {
        String sStateText = "未认证";
        return sStateText;
    }
}
