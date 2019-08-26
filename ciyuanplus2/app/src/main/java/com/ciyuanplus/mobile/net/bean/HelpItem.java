package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class HelpItem implements Serializable {
    @SerializedName("answer")
    public String answer;//答案内容 , ,
    @SerializedName("question")
    public String question;//问题名称
    public boolean isSpeard;// 是否已经展开  默认为false
}
