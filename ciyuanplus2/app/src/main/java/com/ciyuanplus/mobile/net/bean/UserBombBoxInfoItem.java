package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class UserBombBoxInfoItem implements Serializable {
    @SerializedName("isBombBox")
    public int isBombBox;// ,
    @SerializedName("topText")
    public String topText;//
    @SerializedName("boxType")
    public String boxType;// ,
    @SerializedName("boxText")
    public String boxText;// ,
    @SerializedName("buttonText")
    public String buttonText;// ,
    @SerializedName("buttonUrl")
    public String buttonUrl;//
    @SerializedName("isMenuEvent")
    public int isMenuEvent;//
    @SerializedName("isUnread")
    public int isUnread;//
    @SerializedName("menuImage")
    public String menuImage;//
    @SerializedName("menuText")
    public String menuText;//
    @SerializedName("urlMenu")
    public String urlMenu;//

}
