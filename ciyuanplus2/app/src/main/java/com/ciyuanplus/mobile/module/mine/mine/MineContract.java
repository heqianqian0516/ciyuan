package com.ciyuanplus.mobile.module.mine.mine;

import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.net.response.SocialCountItem;

/**
 * Created by Alen on 2017/12/11.
 */

public class MineContract {
    interface Presenter extends BaseContract.Presenter {

        void handleEvent(EventCenterManager.EventMessage eventMessage);

        void handleClick(int id);

        void requestPersonInfo();

        void requestCount();
    }

    interface View extends BaseContract.View {
        void updateView();

        void finishLoadMoreAndRefresh();


        void switchTabSelect(int i);


        void updateInfo(SocialCountItem item);
    }
}
