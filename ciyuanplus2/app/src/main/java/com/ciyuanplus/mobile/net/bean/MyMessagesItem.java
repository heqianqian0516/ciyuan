package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/5/20.
 */

public class MyMessagesItem implements Serializable {
    @SerializedName("title")
    public String title;
    @SerializedName("type")
    public int type; // 用户消息0，关注1， 粉丝2， 系统3， 吐槽4, 小区通讯录5
    @SerializedName("name")
    public String name;
    @SerializedName("message")
    public String message;
    @SerializedName("time")
    public String time;
    @SerializedName("number")
    public int number;

    public MyMessagesItem(int type, int number) {
        this.type = type;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
