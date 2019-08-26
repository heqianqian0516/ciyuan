package com.ciyuanplus.mobile.module.popup.daily_popup;

import android.content.Intent;
import android.widget.RelativeLayout;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class DailyPopupContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);// 初始化数据

        String createShareImage();// 创建图片
    }

    interface View extends BaseContract.View {
        RelativeLayout getPopupLayout();
    }
}
