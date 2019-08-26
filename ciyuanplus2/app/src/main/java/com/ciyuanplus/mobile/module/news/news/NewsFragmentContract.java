package com.ciyuanplus.mobile.module.news.news;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.net.bean.BannerItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class NewsFragmentContract {
    interface Presenter extends BaseContract.Presenter {
        void goSelectTypeActivity();// 跳转到选择显示类型的页面

        void updateZoneName();// 更新小区名

        void updateShowType(int type);

        void clickItem(int postion);

        void doRequest(boolean reset);

        ArrayList<BannerItem> getTopList();

        void handleEventCenterEvent(EventCenterManager.EventMessage eventMessage);
    }

    interface View extends BaseContract.View {
        void setSelectedName(String s);

        void stopRereshAndLoad();

        void updateListView(boolean showList);

        IRecyclerView getListView();

        RecyclerView getQuickEnterListView();

        void updateTopView();

        void changeTopBarVisible(boolean b);

        void showLoadingDialog();

        void dismissLoadingDialog();
    }
}
