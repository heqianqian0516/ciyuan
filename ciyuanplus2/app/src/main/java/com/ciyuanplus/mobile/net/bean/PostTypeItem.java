package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alen on 2017/6/9.
 */

public class PostTypeItem implements Serializable {
    @SerializedName("typeId")
    public final String typeId;// ,
    @SerializedName("typeName")
    public final String typeName;//
    @SerializedName("icon")
    public String icon;// ,

    public PostTypeItem(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
}
