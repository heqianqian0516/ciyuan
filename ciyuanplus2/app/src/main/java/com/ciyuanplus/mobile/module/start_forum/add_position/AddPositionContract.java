package com.ciyuanplus.mobile.module.start_forum.add_position;

import android.content.Intent;

import com.amap.api.maps2d.MapView;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class AddPositionContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent);// 初始化数据
    }

    interface View extends BaseContract.View{
        MapView getMapView();
    }
}
