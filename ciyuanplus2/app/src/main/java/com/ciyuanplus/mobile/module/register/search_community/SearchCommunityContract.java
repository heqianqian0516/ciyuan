package com.ciyuanplus.mobile.module.register.search_community;

import com.ciyuanplus.mobile.adapter.SearchCommunityAdapter;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SearchCommunityContract {
    interface Presenter extends BaseContract.Presenter {
        void doSearchQuery();//进行搜索

        void doSearchQuery(String s);//设置搜索字符串 并且进行搜索

        void returnResult(int postion); // 返回给上一个Activity 当前的选择结果

        void initData(String stringExtra);
    }

    interface View extends BaseContract.View {
        void setSearchText(String keyWord);// 设置搜索内容

        void setCommunityListAdapter(SearchCommunityAdapter mAdapter);// 设置小区列表适配器

        void updateView(int listSize);// 更新页面

        void listStopAnim();// 停止下拉刷新和上拉加载
    }
}
