package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class DailyLabelInfo implements Serializable {
    @SerializedName("yearMonthValue")
    public String yearMonthValue;// ,
    @SerializedName("weekValue")
    public String weekValue;//
    @SerializedName("dayValue")
    public String dayValue;// ,
    @SerializedName("cityValue")
    public String cityValue;// ,
    @SerializedName("weatherValue")
    public String weatherValue;// ,
    @SerializedName("image")
    public String image;//
    @SerializedName("content")
    public String content;//
    @SerializedName("authorUuid")
    public String authorUuid;//
    @SerializedName("authorNickname")
    public String authorNickname;//
    @SerializedName("authorPhoto")
    public String authorPhoto;//
    @SerializedName("showDate")
    public String showDate;//
    @SerializedName("postUuid")
    public String postUuid;//

}
