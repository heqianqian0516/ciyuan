package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2018/1/11.
 */

public class PlaceDetailItem implements Serializable {
    @SerializedName("id")
    public String id;//
    @SerializedName("country")
    public String country;//
    @SerializedName("provinceCode")
    public String provinceCode;//
    @SerializedName("cityCode")
    public String cityCode;//
    @SerializedName("areaCode")
    public String areaCode;//
    @SerializedName("address")
    public String address;//
    @SerializedName("name")
    public String name;//
    @SerializedName("longitude")
    public String longitude;//
    @SerializedName("latitude")
    public String latitude;//
    @SerializedName("score")
    public double score;//
    @SerializedName("wishCount")
    public int wishCount;//
    @SerializedName("rateCount")
    public int rateCount;//
    @SerializedName("indexImg")
    public String indexImg;//
}
