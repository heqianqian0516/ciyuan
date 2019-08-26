package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/5/5.
 */

public class CommunityInfoItem implements Serializable {

    @SerializedName("commAddress")
    public String commAddress;// 想去详细地址 , ,
    @SerializedName("commName")
    public String commName;// 小区名称 ,
    @SerializedName("description")
    public String description;// 小区描述 ,
    @SerializedName("isProperty")
    public int isProperty;// 是否有物业(0:否，1:是) , ,
    @SerializedName("latitude")
    public String latitude;// 小区纬度 , ,
    @SerializedName("longitude")
    public String longitude; // 小区经度 , ,
    @SerializedName("postingNum")
    public int postingNum;// 小区帖子数量 ,
    @SerializedName("propertyId")
    public int propertyId;//物业id ,
    @SerializedName("state")
    public int state;//小区类型:0、系统小区，1、用户创建 ,
    @SerializedName("userNum")
    public int userNum;
    @SerializedName("uuid")
    public String uuid;
}
