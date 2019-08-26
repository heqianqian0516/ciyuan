package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class FriendsItem implements Serializable {
    @SerializedName("currentCommunityUuid")
    public String currentCommunityUuid;//用户当前所在小区 ,
    @SerializedName("followType")
    public int followType;//关注类型 0-被关注|1-我关注对方|2-互相关注 ,
    @SerializedName("identity")
    public int identity;//用户身份 ,
    @SerializedName("isPublish")
    public int isPublish;//冻结状态 ,
    @SerializedName("neibType")
    public int neibType;//邻居类型 0-无|1-邻居 ,
    @SerializedName("nickname")
    public String nickname;//用户昵称 ,
    @SerializedName("photo")
    public String photo;//用户头像 ,
    @SerializedName("sex")
    public int sex;//用户性别 ,,
    @SerializedName("state")
    public int state;//用户状态 ,
    @SerializedName("uuid")
    public String uuid;//用户uuid
}
