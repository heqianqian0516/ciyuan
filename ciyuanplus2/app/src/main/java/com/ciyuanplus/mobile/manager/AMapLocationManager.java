package com.ciyuanplus.mobile.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.statistics.StatisticsManager;

import java.util.List;

/**
 * Created by Alen on 2017/8/29.
 * <p>
 * 实例化一个Amap的对象   异步获取Amap的定位信息，
 * 主要获取经纬度， 如果Amap不给权限， 直接通过本地获取
 */

public class AMapLocationManager {
    private static AMapLocationManager instance = null;
    private final AMapLocationListener mLocationListener;
    private final AMapLocationClient mlocationClient;
    private double longitude = 0.0f;
    private double latitude = 0.0f;
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location location) {
            // 设备位置发生改变时，执行这里的代码
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    };
    private String mCityName = "北京";

    private AMapLocationManager() {
        // 初始化地图定位相关的变量
        mlocationClient = new AMapLocationClient(App.mContext);
        mLocationListener = aMapLocation -> {
            // 定位成功之后进行接口请求
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    longitude = aMapLocation.getLongitude();
                    latitude = aMapLocation.getLatitude();
                    mCityName = aMapLocation.getCity();
                } else {
                    requestPosiFromLocal();
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    StatisticsManager.onErrorInfo("location error", "Amap location error:"
                            + aMapLocation.getErrorCode() + "error info:" + aMapLocation.getErrorInfo());
                }
            } else {
                requestPosiFromLocal();
            }
        };
        mlocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();//启动定位
    }

    public static AMapLocationManager getInstance() {
        if (null == instance) {
            instance = new AMapLocationManager();
        }
        return instance;
    }

    private void requestPosiFromLocal() {
        //位置管理类
        LocationManager locationManager = (LocationManager) App.mContext.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager != null ? locationManager.getProviders(true) : null;
        // 位置提供器
        String provider;
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            //优先使用gps
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        // 更新当前位置
        locationManager.requestLocationUpdates(provider, 10 * 1000, 1,
                locationListener);

    }

    public String getLongitude() {
        mlocationClient.startLocation();//启动定位
        return longitude + "";
    }

    public String getLatitude() {
        return latitude + "";
    }

    public Double getLongitudeDouble() {
        mlocationClient.startLocation();//启动定位
        return longitude;
    }

    public Double getLatitudeDouble() {
        return latitude;
    }

    public String getCityName() {
        return mCityName;
    }

}
