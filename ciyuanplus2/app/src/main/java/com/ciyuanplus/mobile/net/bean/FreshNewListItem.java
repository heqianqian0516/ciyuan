package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class FreshNewListItem implements Serializable {
    @SerializedName("list")
    public FreshNewItem[] list;//小区名称
}
