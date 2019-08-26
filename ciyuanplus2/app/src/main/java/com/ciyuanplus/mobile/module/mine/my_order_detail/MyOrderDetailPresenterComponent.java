package com.ciyuanplus.mobile.module.mine.my_order_detail;

import dagger.Component;

/**
 * Created by kk on 2018/5/24.
 */

@Component(modules = {MyOrderDetailPresenterModule.class})
public interface MyOrderDetailPresenterComponent {
    void inject(MyOrderDetailActivity myOrderDetailActivity);
}
