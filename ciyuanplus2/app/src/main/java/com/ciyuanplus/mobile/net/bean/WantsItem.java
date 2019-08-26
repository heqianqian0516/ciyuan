package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alen on 2018/1/9.
 */

public class WantsItem {
    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;// 用户当前所在小区 ,
    @SerializedName("nickname")
    public String nickname;// ,用户昵称 ,
    @SerializedName("photo")
    public String photo;// ,用户头像 ,
    @SerializedName("placeId")
    public String placeId;// ,地点id ,
    @SerializedName("sex")
    public int sex;// ,用户性别
    @SerializedName("uuid")
    public String uuid;// ,用户uuid
}
