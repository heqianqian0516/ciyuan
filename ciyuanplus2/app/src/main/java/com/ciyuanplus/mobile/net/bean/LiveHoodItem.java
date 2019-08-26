package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class LiveHoodItem implements Serializable {
    @SerializedName("allowedUserState")
    public int allowedUserState;//允许点击的用户类型 0全部 1正式用户 ,
    @SerializedName("id")
    public String id;//民生ID ,
    @SerializedName("img")
    public String img;//民生图片 ,
    @SerializedName("name")
    public String name;//民生名称 ,
    @SerializedName("orderNum")
    public int orderNum;//排序字段
    @SerializedName("updateTime")
    public String updateTime;//后修改时间 ,
    @SerializedName("url")
    public String url;// 民生入口
}
