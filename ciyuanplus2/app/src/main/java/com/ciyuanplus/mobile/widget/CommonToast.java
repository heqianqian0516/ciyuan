package com.ciyuanplus.mobile.widget;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;


/**
 * Created by Alen on 2016/11/26.
 * 通用的toast， 防止弹出toast延时
 */
public class CommonToast {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static Toast getInstance(String alertString, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(App.mContext, alertString, time);
        } else {
            mToast.setText(alertString);
            mToast.setDuration(time);
        }
        return mToast;
    }

    @SuppressLint("ShowToast")
    public static Toast getInstance(String alertString) {
        if (mToast == null) {
            mToast = Toast.makeText(App.mContext, alertString, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(alertString);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        return mToast;
    }
}
