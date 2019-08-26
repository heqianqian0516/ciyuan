package com.ciyuanplus.mobile.module.wiki.wiki_position;

import android.content.Intent;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.utils.Constants;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class WikiPositionPresenter implements WikiPositionContract.Presenter {
    private final WikiPositionContract.View mView;
    private WikiItem mWikiItem;
    private AMap aMap;

    @Inject
    public WikiPositionPresenter(WikiPositionContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent) {
        mWikiItem = (WikiItem) intent.getSerializableExtra(Constants.INTENT_POSITION_ITEM);
        aMap = mView.getMapView().getMap();
        aMap.animateCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel() - 2), 0, null); // 3-19 度的缩放
        makepoint();
    }

    //根据地址绘制需要显示的点
    private void makepoint() {
        //北纬39.22，东经116.39，为负则表示相反方向
        LatLng latLng = new LatLng(Double.parseDouble(mWikiItem.latitude), Double.parseDouble(mWikiItem.longitude));

        //使用默认点标记
        Marker maker = aMap.addMarker(new MarkerOptions().position(latLng).title("" + mWikiItem.name).snippet(mWikiItem.address));
        maker.showInfoWindow();

        //自定义点标记
//        MarkerOptions markerOptions=new MarkerOptions();
//        markerOptions.position(new LatLng(34,115)).title("标题").snippet("内容");
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(),R.mipmap.ic_launcher)));//设置图标
//        aMap.addMarker(markerOptions);

        //改变可视区域为指定位置
        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 8, 0, 30));
        aMap.moveCamera(cameraUpdate);//地图移向指定区域

        //位置坐标的点击事件
        aMap.setOnMarkerClickListener((marker) -> false);
        //位置上面信息窗口的点击事件
        aMap.setOnInfoWindowClickListener((marker) -> {
        });
    }
}
