package com.ciyuanplus.mobile.net.bean;

import com.ciyuanplus.mobile.module.mine.newbie_task.NewbietaskActivity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewTaskListBean implements Serializable {
    @SerializedName("data")
    public NewTaskBean data;
}
