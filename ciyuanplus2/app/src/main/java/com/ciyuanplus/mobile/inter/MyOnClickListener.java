package com.ciyuanplus.mobile.inter;

import android.view.View;

import com.ciyuanplus.mobile.statistics.StatisticsManager;

/**
 * Created by Alen on 2017/11/22.
 * <p>
 * 添加统一打点
 */

public abstract class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        StatisticsManager.onEventInfo(v.getContext().getClass().getName(),
                v.getResources().getResourceName(v.getId()), "OnClick");

        performRealClick(v);
    }

    protected abstract void performRealClick(View v);

}
