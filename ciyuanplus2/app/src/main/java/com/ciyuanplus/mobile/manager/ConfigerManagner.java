package com.ciyuanplus.mobile.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/21 9:02 PM
 * class  : ConfigerManagner
 * desc   : 只是用来保存一些cookie信息
 * version: 1.0
 */
public class ConfigerManagner {
    private static ConfigerManagner configManger;
    private static Context context;

    ConfigerManagner(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ConfigerManagner getInstance(Context context) {
        if (configManger == null) {
            configManger = new ConfigerManagner(context);
        }
        return configManger;
    }

    protected SharedPreferences getMySharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean setString(String name, String value) {
        return getMySharedPreferences().edit().putString(name, value).commit();
    }

    public String getString(String name) {
        return getMySharedPreferences().getString(name, "");
    }
}
