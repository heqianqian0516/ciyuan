package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.OrderDetailItem;

import java.util.List;

/**
 * Created by kk on 2018/5/25.
 */

class OrderDetailResponse extends ResponseData {

    public List<OrderDetailItem> orderDetailItems;

    public OrderDetailResponse(String data) {
        super(data);

//        if (!Utils.isStringEquals(mCode, CODE_OK)) return;
//
//        OrderDetailList resultBean = new Gson().fromJson(data,OrderDetailList.class);
//        //对象中拿到集合
//        orderDetailItems = resultBean.data;

    }
}
