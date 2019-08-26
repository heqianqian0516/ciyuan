package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class WorldUserItem implements Serializable {
    @SerializedName("identity")
    public int identity;//用户身份 ,
    @SerializedName("isFollow")
    public int isFollow;//当前用户是否关注了此用户(0:否，1:是， 2: 互相关注) ,
    @SerializedName("nickname")
    public String nickname;//用户昵称 ,
    @SerializedName("photo")
    public String photo;//用户头像 ,
    @SerializedName("py")
    public String py;//用户昵称首拼 ,
    @SerializedName("sex")
    public int sex;//
    @SerializedName("uuid")
    public String uuid;//
}
