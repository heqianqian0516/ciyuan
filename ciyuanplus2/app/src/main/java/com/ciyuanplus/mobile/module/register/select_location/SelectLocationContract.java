package com.ciyuanplus.mobile.module.register.select_location;

import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.ciyuanplus.mobile.adapter.SelectPosiAdapter;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SelectLocationContract {
    interface Presenter extends BaseContract.Presenter {
        void locationListSelected(int postion);// 列表选中监听器

        void initData(String fromType, MapView mSelectLocationMap);// 初始化数据

        void doSearchQuery(LatLonPoint lp);// 按照位置进行搜索

        void addCommunity();// 添加小区

        void goSearchActivity();// 跳转到搜索小区的页面

        void moveCamera(double latitude, double longitude);// 地图移动到某个位置为中心的视图
    }

    interface View extends BaseContract.View {
        void setLocationListAdapter(SelectPosiAdapter adapter); // 设置搜索结果列表适配器
    }
}
