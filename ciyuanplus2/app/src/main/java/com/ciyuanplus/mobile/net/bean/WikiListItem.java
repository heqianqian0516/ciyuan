package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/9/19.
 */

public class WikiListItem implements Serializable {
    @SerializedName("list")
    public WikiItem[] list;//
}
