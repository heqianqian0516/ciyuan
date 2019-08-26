package com.ciyuanplus.mobile.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @Created SiberiaDante
 * @Describe：
 * @CreateTime: 2017/12/18
 * @UpDateTime:
 * @Email: 2654828081@qq.com
 * @GitHub: https://github.com/SiberiaDante
 */

class ScreenUtil {

    /**
     * @return 获取屏幕的高 单位：px
     */
    public static int getScreenHeightPx(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);
        }
        return dm.heightPixels;

    }
}
