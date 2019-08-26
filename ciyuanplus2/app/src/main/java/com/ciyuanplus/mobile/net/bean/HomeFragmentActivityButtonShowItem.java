package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kk on 2018/5/18.
 */

public class HomeFragmentActivityButtonShowItem {

    @SerializedName("imageUrl")
    public final String imageUrl;
    @SerializedName("isShowButton")
    public final boolean isShowButton;

    public HomeFragmentActivityButtonShowItem(String url, boolean isShowButton) {
        this.imageUrl = url;
        this.isShowButton = isShowButton;
    }
}
