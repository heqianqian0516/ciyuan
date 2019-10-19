package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskNumberListBean implements Serializable {
    @SerializedName("data")
    public TaskNumberBean data;
}
