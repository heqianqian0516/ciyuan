package com.ciyuanplus.mobile.net.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kk on 2018/5/2.
 */

class WechatAuthInfo implements Serializable {

    /**
     * activityImg1 : string
     * activityImg2 : string
     * height : 0
     * isActivityGameBox : 0
     * isHaveGame : 0
     * isNeedJumpBindWX : 0
     * webViewUrl : string
     * width : 0
     */

    @SerializedName("activityImg1")
    public String activityImg1;
    @SerializedName("activityImg2")
    public String activityImg2;
    @SerializedName("height")
    public int height;
    @SerializedName("isActivityGameBox")
    public int isActivityGameBox;
    @SerializedName("isHaveGame")
    public int isHaveGame;
    @SerializedName("isNeedJumpBindWX")
    public int isNeedJumpBindWX;
    @SerializedName("webViewUrl")
    public String webViewUrl;
    @SerializedName("width")
    public int width;


}
