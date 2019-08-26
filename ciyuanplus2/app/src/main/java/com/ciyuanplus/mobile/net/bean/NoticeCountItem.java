package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class NoticeCountItem implements Serializable {
    @SerializedName("feedbackMessageCount")
    public int feedbackMessageCount;//
    @SerializedName("followCount")
    public int followCount;//
    @SerializedName("postCount")
    public int postCount;//
    @SerializedName("systemMessageCount")
    public int systemMessageCount;//

}
