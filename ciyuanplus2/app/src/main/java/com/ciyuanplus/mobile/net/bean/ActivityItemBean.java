package com.ciyuanplus.mobile.net.bean;

import com.ciyuanplus.mobile.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/5/5.
 */

public class ActivityItemBean implements Serializable {
    @SerializedName("code")
    public String code;// 用户当前所在小区 ,
    @SerializedName("msg")
    public String msg;// 用户当前所在小区 ,
}
