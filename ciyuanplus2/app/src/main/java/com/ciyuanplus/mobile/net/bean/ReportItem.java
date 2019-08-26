package com.ciyuanplus.mobile.net.bean;

/**
 * Created by Alen on 2017/8/22.
 */

public class ReportItem {
    public final String name;
    public boolean isChecked;
    public final String id;

    public ReportItem(String id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }
}
