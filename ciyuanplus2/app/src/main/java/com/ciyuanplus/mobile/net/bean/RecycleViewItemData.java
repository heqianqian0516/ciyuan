package com.ciyuanplus.mobile.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kk on 2018/5/29.
 */

public class RecycleViewItemData<T> {

    //用来装载不同类型的item数据bean
    @SerializedName("t")
    public T t;
    //item数据bean的类型
    @SerializedName("dataType")
    public int dataType;

    public RecycleViewItemData() {
    }

    public RecycleViewItemData(T t, int dataType) {
        this.t = t;
        this.dataType = dataType;
    }
}
