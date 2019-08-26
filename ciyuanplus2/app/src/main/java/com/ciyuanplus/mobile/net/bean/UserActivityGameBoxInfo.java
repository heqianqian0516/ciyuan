package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 用户游戏活动弹框信息
 * Created by kk on 2018/5/17.
 */

public class UserActivityGameBoxInfo {

    //    activityImg1 (string, optional): 活动图片1 ,
//    activityImg2 (string, optional): 活动图片2 ,
//    bindWXTipText (string, optional): 绑定微信提示文字 ,
//    height (integer, optional): 高度 ,
//    isActivityGameBox (integer, optional): 是否有弹窗(0:无;1:有) ,
//    isHaveGame (integer, optional): 是否有活动(0:无;1:有) ,
//    isNeedJumpBindWX (integer, optional): 是否需要跳转绑定微信（1：需要，0：不需要） ,
//    webViewUrl (string, optional): webview链接地址 ,
//    width (integer, optional): 宽度
    @SerializedName("activityImg1")
    public String activityImg1;
    @SerializedName("activityImg2")
    public String activityImg2;
    @SerializedName("bindWXTipText")
    public String bindWXTipText;
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
