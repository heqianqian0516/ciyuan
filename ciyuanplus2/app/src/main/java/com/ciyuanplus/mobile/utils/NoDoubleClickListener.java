package com.ciyuanplus.mobile.utils;

import android.view.View;

import java.util.Calendar;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private static long lastClickTime = 0;

    public static final int MIN_CLICK_DELAY_TIME = 700;

    public abstract void onNoDoubleClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);

        }
    }
}
