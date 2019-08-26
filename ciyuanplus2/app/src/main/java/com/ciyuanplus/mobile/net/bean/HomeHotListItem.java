package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alen on 2017/6/9.
 */

public class HomeHotListItem implements Serializable {
    @SerializedName("list")
    public ArrayList<FreshNewItem> list;//小区名称
}
