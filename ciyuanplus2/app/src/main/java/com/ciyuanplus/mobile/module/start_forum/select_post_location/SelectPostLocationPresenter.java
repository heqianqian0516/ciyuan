package com.ciyuanplus.mobile.module.start_forum.select_post_location;

import android.app.Activity;
import android.content.Intent;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ciyuanplus.mobile.adapter.SelectPostLocationAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class SelectPostLocationPresenter implements SelectPostLocationContract.Presenter, PoiSearch.OnPoiSearchListener {
    private final SelectPostLocationContract.View mView;
    private String keyWord = "";
    private SelectPostLocationAdapter mAdapter;
    private final int PAGE_SIZE = 100;
    private int mCurrentPage = 0;
    private String mCity;
    private final ArrayList<PoiItem> mPoiList = new ArrayList<>();
    private final String key_type = Constants.ALL_LOCATION_SEARCH_KEY_TYPE;
    @Inject
    public SelectPostLocationPresenter(SelectPostLocationContract.View mView) {
        this.mView = mView;
        mCity = AMapLocationManager.getInstance().getCityName();

        mAdapter = new SelectPostLocationAdapter(mView.getDefaultContext(), mPoiList, (v)->{
            int postion = mView.getListView().getChildLayoutPosition(v) - 2;
            PoiItem item = mAdapter.getItem(postion);
            if(Utils.isStringEquals(item.getPoiId() ,"-1")){// 跳转到新增地址页面  暂时没用到

            } else {
                Intent intent = new Intent();
                intent.putExtra(Constants.INTENT_COMMUNITY_ITEM, new Gson().toJson(item));
                ((Activity)mView.getDefaultContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity)mView.getDefaultContext()).finish();
            }

        });
        mView.getListView().setIAdapter(mAdapter);
    }

    @Override
    public void initData(Intent intent) {
        String type = intent.getStringExtra(Constants.INTENT_ACTIVITY_TYPE);

//        key_type = Utils.isStringEquals(mType, StartFoodActivity.class.getSimpleName())
//                ? Constants.FOOD_LOCATION_SEARCH_KEY_TYPE : Constants.LIVE_LOCATION_SEARCH_KEY_TYPE;
    }

    private void doSearchQuery() {
        PoiSearch.Query query = new PoiSearch.Query(keyWord + "", key_type, mCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(PAGE_SIZE);// 设置每页最多返回多少条poiitem, 如果前50条里面还没有你的小区， 试着移动下位置
        query.setPageNum(mCurrentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(mView.getDefaultContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    private void doSearchQuery(LatLonPoint lp) {
        PoiSearch.Query query = new PoiSearch.Query("", key_type, "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(PAGE_SIZE);// 设置每页最多返回多少条poiitem, 如果前50条里面还没有你的小区， 试着移动下位置
        query.setPageNum(mCurrentPage);// 设置查第一页

        if (lp != null) {
            PoiSearch poiSearch = new PoiSearch(mView.getDefaultContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));//
            // 设置搜索区域为以lp点为圆心，其周围2000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void doSearchQuery(String s) {
        keyWord = s;
        if (!Utils.isStringEmpty(keyWord)) {
            mCurrentPage = 0;
            doSearchQuery();
        } else {
            doSearchQuery(new LatLonPoint(AMapLocationManager.getInstance().getLatitudeDouble(),
                    AMapLocationManager.getInstance().getLongitudeDouble()));
        }
    }

    @Override
    public void detachView() {
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                List<PoiItem> poiItems = poiResult.getPois();
                if (mCurrentPage == 0) mPoiList.clear();

                mPoiList.addAll(poiItems);
//                if(!Utils.isStringEmpty(keyWord)){// 当进行搜索的时候  需要添加  自建地址 的选项， 这里用id 为-1 进行处理。
//                    PoiItem addItem = new PoiItem("-1", null, "没有找到您的位置", "创建新的位置:" + keyWord);
//                    mPoiList.add(addItem);
//                }
                mCurrentPage++;
                mAdapter.notifyDataSetChanged();
            }
        } else {
            CommonToast.getInstance("未搜索到小区").show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
