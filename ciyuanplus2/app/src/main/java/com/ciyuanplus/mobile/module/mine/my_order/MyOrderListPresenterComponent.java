package com.ciyuanplus.mobile.module.mine.my_order;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MyOrderListPresenterModule.class})
public interface MyOrderListPresenterComponent {
    void inject(MyOrderListActivity mActivity);
}
