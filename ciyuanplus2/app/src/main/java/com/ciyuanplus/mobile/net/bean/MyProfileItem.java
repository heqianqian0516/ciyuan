package com.ciyuanplus.mobile.net.bean;


/**
 * Created by Alen on 2017/5/17.
 * 我的个人中心 编辑页面  里面的列表项
 */

public class MyProfileItem {
    public static final String TYPE_HEAD = "head_icon";
    public static final String TYPE_NAME = "name";
    public static final String TYPE_SEX = "sex";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_PASSWORD = "password";
    public static final String TYPE_IDENTIFY = "identify";
    public static final String TYPE_ADDRESS = "address";
    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_CACHE = "cache";
    public static final String TYPE_BIRTHDAY = "birthday";
    public static final String TYPE_HELP = "help";
    public static final String TYPE_ABOUT = "about";
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_CHANGE_SERVER = "change_server";
    public static final String TYPE_ACCOUNT_MANAGE = "manage_account";
    public static final String TYPE_SIGN = "type_sign";

    public final String type;
    public final String name;
    public String value;
    private int isWXBind;
    private int isWeiBoBind;
    private int isQQBind;

    public MyProfileItem(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public MyProfileItem(String type, String name, String value, int isWXBind, int isWeiBoBind, int isQQBind) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isWXBind = isWXBind;
        this.isWeiBoBind = isWeiBoBind;
        this.isQQBind = isQQBind;
    }

    public int getIsWXBind() {
        return isWXBind;
    }

    public void setIsWXBind(int isWXBind) {
        this.isWXBind = isWXBind;
    }

    public int getIsWeiBoBind() {
        return isWeiBoBind;
    }

    public void setIsWeiBoBind(int isWeiBoBind) {
        this.isWeiBoBind = isWeiBoBind;
    }

    public int getIsQQBind() {
        return isQQBind;
    }

    public void setIsQQBind(int isQQBind) {
        this.isQQBind = isQQBind;
    }
}
