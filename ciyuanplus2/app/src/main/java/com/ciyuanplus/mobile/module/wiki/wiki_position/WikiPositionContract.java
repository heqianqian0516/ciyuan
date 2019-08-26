package com.ciyuanplus.mobile.module.wiki.wiki_position;

import android.content.Intent;

import com.amap.api.maps2d.MapView;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class WikiPositionContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);
    }

    interface View extends BaseContract.View {
        MapView getMapView();
    }
}
