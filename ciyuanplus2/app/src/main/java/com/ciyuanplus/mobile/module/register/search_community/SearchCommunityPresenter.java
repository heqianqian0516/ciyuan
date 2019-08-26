package com.ciyuanplus.mobile.module.register.search_community;

import android.app.Activity;
import android.content.Intent;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ciyuanplus.mobile.adapter.SearchCommunityAdapter;
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

public class SearchCommunityPresenter implements SearchCommunityContract.Presenter, PoiSearch.OnPoiSearchListener {
    private String keyWord = "";
    private final SearchCommunityContract.View mView;
    private SearchCommunityAdapter mAdapter;
    private int mCurrentPage = 0;
    private String mCity = "北京";
    private final List<PoiItem> mPoiList = new ArrayList<>();

    @Inject
    public SearchCommunityPresenter(SearchCommunityContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void detachView() {
    }


    public void doSearchQuery() {
        PoiSearch.Query query = new PoiSearch.Query(keyWord + "", Constants.ZONE_SEARCH_KEY_TYPE, mCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        int PAGE_SIZE = 50;
        query.setPageSize(PAGE_SIZE);// 设置每页最多返回多少条poiitem, 如果前50条里面还没有你的小区， 试着移动下位置
        query.setPageNum(mCurrentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(mView.getDefaultContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void doSearchQuery(String s) {
        keyWord = s;
        if (!Utils.isStringEmpty(keyWord)) {
            mCurrentPage = 0;
            doSearchQuery();
        }

    }

    @Override
    public void returnResult(int postion) {
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_COMMUNITY_ITEM, new Gson().toJson(mAdapter.getItem(postion)));
        ((Activity) mView.getDefaultContext()).setResult(Activity.RESULT_OK, intent);
        ((Activity) mView.getDefaultContext()).finish();
    }

    @Override
    public void initData(String stringExtra) {
        if (Utils.isStringEmpty(stringExtra)) mCity = "北京";// 如果不开启权限，获取不到定位信息  默认搜索北京
        else mCity = stringExtra;

        mView.setSearchText(keyWord);
        mCurrentPage = 0;

        mAdapter = new SearchCommunityAdapter(mView.getDefaultContext(), mPoiList);
        mView.setCommunityListAdapter(mAdapter);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        mView.listStopAnim();

        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                List<PoiItem> poiItems = poiResult.getPois();
                if (mCurrentPage == 0) mPoiList.clear();

                mPoiList.addAll(poiItems);
                mCurrentPage++;
                mAdapter.setmList(mPoiList);
                mView.updateView(mPoiList.size());
            }
        } else {
            CommonToast.getInstance("未搜索到小区").show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
