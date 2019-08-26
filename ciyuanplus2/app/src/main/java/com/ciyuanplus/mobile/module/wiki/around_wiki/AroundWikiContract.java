package com.ciyuanplus.mobile.module.wiki.around_wiki;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.module.BaseContract;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class AroundWikiContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();//初始化数据

        void requestWikiList(boolean b);// 请求数据  b 表示是否需要清除当前数据
    }

    interface View extends BaseContract.View {
        RecyclerView getTopWikiTypeGrid();// 顶部的wiki 分类RecyclerView

        IRecyclerView getWikiList();//  Wiki 列表IRecyclerView

        void updateView(boolean b);// 刷新无数据 视图， b代表是否有数据

        void stopRefreshAndLoadMore(); // 列表停止刷新动画

        void setLoadStatueTheEnd(); // 列表数据到最底部  再无其他数据
    }
}
