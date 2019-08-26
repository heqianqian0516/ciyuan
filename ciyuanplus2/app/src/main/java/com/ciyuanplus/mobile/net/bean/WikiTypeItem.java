package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class WikiTypeItem implements Serializable {
    @SerializedName("id")
    public final String id;// ,
    @SerializedName("name")
    public final String name;//

    public WikiTypeItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
