package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class MarketSortItem implements Serializable {
    @SerializedName("sortId")
    public final String sortId;//
    @SerializedName("sortName")
    public final String sortName;//
    public final boolean isSelected;

    public MarketSortItem(String sortId, String sortName, boolean isSelected) {
        this.sortId = sortId;
        this.sortName = sortName;
        this.isSelected = isSelected;
    }
}
