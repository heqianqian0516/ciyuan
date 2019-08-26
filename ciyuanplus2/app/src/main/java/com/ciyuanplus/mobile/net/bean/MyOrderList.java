package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kk on 2018/5/24.
 */

public class MyOrderList {
    @SerializedName("data")
    public List<MyOrderItem> data;

    @SerializedName("pager")
    public String pager;
}
