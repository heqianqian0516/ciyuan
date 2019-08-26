package com.ciyuanplus.mobile.module.register.select_location;

import android.app.Activity;
import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.adapter.SelectPosiAdapter;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.register.search_community.SearchCommunityActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.AddCommunityApiParameter;
import com.ciyuanplus.mobile.net.parameter.UserAddCommunityApiParameter;
import com.ciyuanplus.mobile.net.response.AddCommunityResponse;
import com.ciyuanplus.mobile.net.response.UserAddCommunityResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class SelectLocationPresenter implements SelectLocationContract.Presenter, AMap.OnCameraChangeListener, LocationSource, PoiSearch.OnPoiSearchListener, AMapLocationListener {
    public static final String ACTIVITY_FROM_ADDRESSMANAGER = "AddressManagerActivity";
    public PoiItem mPoiItem;
    private String mFromType;
    private final SelectLocationContract.View mView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private SelectPosiAdapter mPoiAdapter;
    private String mCityName;

    @Inject
    public SelectLocationPresenter(SelectLocationContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(String fromType, MapView selectLocationMap) {
        mFromType = fromType;
        aMap = selectLocationMap.getMap();
        aMap.animateCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel() - 2), 0, null); // 3-19 度的缩放
        aMap.setOnCameraChangeListener(this);

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.amap_location_icon));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(0x00000000);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(0x00000000);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void locationListSelected(int postion) {
        final PoiItem item = (PoiItem) mPoiAdapter.getItem(postion);
        CustomDialog.Builder builder = new CustomDialog.Builder(mView.getDefaultContext());
        builder.setMessage("确定选择该" + item.getTitle() + "?");
        builder.setPositiveButton("确定", (dialog, which) -> {
            mPoiItem = item;
            mPoiAdapter.notifyDataSetChanged();
            dialog.dismiss();
            addCommunity();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
            mPoiAdapter.notifyDataSetChanged();
        });
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SELECT_COMMUNITY,
                StatisticsConstant.OP_SELECT_COMMUNITY_SELECT_ITEM_DIALOG_SHOW,
                Utils.isStringEquals(ACTIVITY_FROM_ADDRESSMANAGER, mFromType) ? "addressManager" : "default");

    }

    @Override
    public void detachView() {
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        CameraPosition centerPosi = cameraPosition;
//        CommonToast.getInstance("当前位置" + cameraPosition.target.latitude + cameraPosition.target.longitude).show();
        doSearchQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(mView.getDefaultContext());
            //初始化定位参数
            AMapLocationClientOption locationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(locationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    // 添加小区   如果该小区已经有了， 那么直接返回uuid
    public void addCommunity() {
        if (mPoiItem == null) {
            CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_select_location_add_community_empty_alert)).show();
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_ADD_COMMUNITY_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommunityApiParameter(mPoiItem.getTitle(), mPoiItem.getProvinceCode(),
                mPoiItem.getCityCode(), mPoiItem.getAdCode(), mPoiItem.getSnippet(),
                mPoiItem.getLatLonPoint().getLongitude() + "", mPoiItem.getLatLonPoint().getLatitude() + "",
                AddCommunityApiParameter.COMMUNITY_TYPE_DEFAULT).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                AddCommunityResponse response1 = new AddCommunityResponse(s);
                SharedPreferencesManager.putString("Pass", "communityUuid", response1.communityInfoItem.uuid);
                SharedPreferencesManager.putString("MyAddress", "address", response1.communityInfoItem.commName);
                System.out.println(response1.communityInfoItem.commName + "测试2");
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();

                } else {
                    userAddCommunity(response1.communityInfoItem.uuid, response1.communityInfoItem.commName);
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_select_location_user_add_community_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void goSearchActivity() {
        Intent intent = new Intent(mView.getDefaultContext(), SearchCommunityActivity.class);
        intent.putExtra(Constants.INTENT_SEARCH_CITY, mCityName);
        ((SelectLocationActivity) mView.getDefaultContext()).startActivityForResult(intent, Constants.REQUEST_CODE_SEARCH_COMMUNITY);
    }

    @Override
    public void moveCamera(double latitude, double longitude) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),
                aMap.getMaxZoomLevel() - 2));
    }


    // 用户加入小区
    private void userAddCommunity(final String communityId, final String communityName) {
        String userid = UserInfoData.getInstance().getUserInfoItem().uuid;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_USER_ADD_COMMUNITY_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UserAddCommunityApiParameter(userid, communityId).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                UserAddCommunityResponse response1 = new UserAddCommunityResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid = communityId;
                    UserInfoData.getInstance().getUserInfoItem().currentCommunityName = communityName;

                    if (Utils.isStringEquals(ACTIVITY_FROM_ADDRESSMANAGER, mFromType)) {
                        CommunityManager.getInstance().getCommunityListFromNet();// 更新一下用户的小区列表
                        EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH));

                        ((Activity) mView).finish();
                    } else {
                        ////RongManager.connect();
                        Intent intent = new Intent(mView.getDefaultContext(), MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Animeter", true);
                        mView.getDefaultContext().startActivity(intent);
                        ((Activity) mView).finish();
                    }
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_select_location_user_add_community_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void doSearchQuery(LatLonPoint lp) {
        int currentPage = 0;
        PoiSearch.Query query = new PoiSearch.Query("", Constants.ZONE_SEARCH_KEY_TYPE, "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(50);// 设置每页最多返回多少条poiitem, 如果前50条里面还没有你的小区， 试着移动下位置
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            PoiSearch poiSearch = new PoiSearch(mView.getDefaultContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));//
            // 设置搜索区域为以lp点为圆心，其周围2000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                List<PoiItem> poiItems = poiResult.getPois();
                mPoiItem = null;
                mPoiAdapter = new SelectPosiAdapter((SelectLocationActivity) mView.getDefaultContext(), poiItems);
                mView.setLocationListAdapter(mPoiAdapter);
                mPoiAdapter.notifyDataSetChanged();
            }
        }  //CommonToast.getInstance(rcode + "").show();

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                mCityName = aMapLocation.getCity();

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            }
        }
    }
}
