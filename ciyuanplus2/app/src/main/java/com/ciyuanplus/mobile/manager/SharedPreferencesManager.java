package com.ciyuanplus.mobile.manager;

import android.content.Context;

import com.ciyuanplus.mobile.App;


/**
 * Created by Alen on 2016/11/29.
 */
public class SharedPreferencesManager {

    public static boolean getBoolean(String name, String key, boolean defValue) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static boolean putBoolean(String name, String key, boolean value) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
                .putBoolean(key, value).commit();
    }

    public static String getString(String name, String key, String defValue) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, "");
    }

    public static boolean putString(String name, String key, String value) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }

    public static int getInt(String name, String key, int defValue) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static boolean putInt(String name, String key, int value) {
        return App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
                .putInt(key, value).commit();
    }

    // 尽量不要用这个操作， 比较危险，
    // 会清除当前 set下的所有数据。
    public static void clearAllSets(String name) {
        App.mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
