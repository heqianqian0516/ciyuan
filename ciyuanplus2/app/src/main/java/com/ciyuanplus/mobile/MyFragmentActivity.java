package com.ciyuanplus.mobile;

import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.umeng.analytics.MobclickAgent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by Alen on 2017/6/25.
 */

public class MyFragmentActivity extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StatisticsManager.onEventInfo(getClass().getName(), "onCreate");// 这里最好不要用 getClass().getSimpleName()，因为可能存在不同文件夹下面的相同类名
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);// 用户Umeng 统计，
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);// 用户Umeng 统计，
    }

    // 打点可以 直接调用这个函数， 如果需要在Activity 里面写 onClickListener 都要先调用这个方法。
    public void onViewClicked(View v) {
        StatisticsManager.onEventInfo(v.getContext().getClass().getName(),
                v.getResources().getResourceName(v.getId()), "OnClick");
    }
}
