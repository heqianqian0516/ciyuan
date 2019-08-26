package com.ciyuanplus.mobile;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Alen on 2017/6/25.
 */

public class MyFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        StatisticsManager.onEventInfo(getClass().getName(), "onCreate");// 这里最好不要用 getClass().getSimpleName()，因为可能存在不同文件夹下面的相同类名

        MobclickAgent.onPageStart(getClass().getSimpleName()); //Umeng 统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName()); //Umeng 统计页面
    }

    // 打点可以 直接调用这个函数， 如果需要在Fragment 里面写 onClickListener 都要先调用这个方法。
    public void onViewClicked(View v) {
//        StatisticsManager.onEventInfo(v.getContext().getClass().getName(),
//                v.getResources().getResourceName(v.getId()), "OnClick");
    }
}
