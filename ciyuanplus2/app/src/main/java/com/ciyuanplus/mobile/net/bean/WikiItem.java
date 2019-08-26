package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class WikiItem implements Serializable {
    @SerializedName("address")
    public String address;// ,
    @SerializedName("invalidNum")
    public int invalidNum;//
    @SerializedName("isInvalid")
    public int isInvalid;// ,
    @SerializedName("isValid")
    public int isValid;// ,
    @SerializedName("name")
    public String name;// ,
    @SerializedName("telephone")
    public String telephone;// ,
    @SerializedName("id")
    public String id;//
    @SerializedName("uuid")
    public String uuid;//
    @SerializedName("validNum")
    public int validNum;// ,
    @SerializedName("wikiTypeParentId")
    public int wikiTypeParentId;// ,
    @SerializedName("image")
    public String image;//
    @SerializedName("latitude")
    public String latitude;// 小区纬度 , ,
    @SerializedName("longitude")
    public String longitude; // 小区经度 , ,
    @SerializedName("placeId")
    public String placeId;//
    @SerializedName("score")
    public String score;// ,
    @SerializedName("userCount")
    public int userCount;// ,
    @SerializedName("userImgs")
    public String[] userImgs;//
}
