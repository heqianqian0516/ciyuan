package com.ciyuanplus.mobile.utils;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * Created by 小狼 on 2018/3/27.
 */

class TabUtils {


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip, int bottomDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        int bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottomDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {

            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            params.bottomMargin = bottom;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
