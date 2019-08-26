package com.ciyuanplus.mobile.module.mine.my_order_detail;

import android.content.Intent;
import android.widget.EditText;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by kk on 2018/5/24.
 */

class MyOrderDetailContract implements BaseContract {

    public interface View extends BaseContract.View {

        void updatePage(boolean show);

        void setBuyer(String buyer);

        void setBuyerPhone(String phone);

        void setBuyerAddress(String address);

        void setProductImage(String imageUrl);

        void setProductPrice(String productPrice);

        void setProductTitle(String productTitle);

        void setBuyCount(String buyCount);

        void setOrderPrice(String orderPrice);

        void setDeliveryMethod(String deliveryMethod);

        void setDeliveryMethodBottom(String deliveryMethodBottom);

        void setOrderID(String orderID);

        void setOrderTime(String orderTime);

        void showCancelOrderLayout(boolean show);

        void showOrderStatusLayout(boolean show);

        void showCancelOrderButton(boolean show);

        void showOrderButton(boolean show);

        void showmEditLogistticsInfo(boolean show);

        void showNoticeText(boolean show);

        void showmDeliveryLogistitcs(boolean show);

        String getLogistticsInfo();

        void setNoticeText(String notice);

        void setDeliveryLogistitcs(String deliveryLogistitcs);

        EditText getLogisticsInfoView();

    }

    public interface Presenter extends BaseContract.Presenter {

        void initData(Intent intent);

        void acceptOrder();

        void cancelOrder();
    }
}
