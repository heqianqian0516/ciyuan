package com.ciyuanplus.mobile.module.mine.my_order;

import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.net.bean.MyOrderItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by kk on 2018/5/24.
 */

class MyOrderListContract implements BaseContract {

    public interface View extends BaseContract.View {

        RecyclerView getMainList();

        void showEmptyView();

        void setLoadMoreEnable(boolean enable);

        void finishLoadMoreAndRefresh();

        void updateView(List<MyOrderItem> orderItemList);

        void toH5(String url);

    }

    public interface Presenter extends BaseContract.Presenter {

        void doRequest(boolean reset, String merId, String status);
    }
}
