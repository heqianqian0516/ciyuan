package com.ciyuanplus.mobile.module.mine.my_order_detail;

import android.content.Intent;

import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.OrderDetailItem;
import com.ciyuanplus.mobile.net.bean.OrderDetailList;
import com.ciyuanplus.mobile.net.parameter.RequestOrderConfirmApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestOrderDetailApiParameter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import static com.ciyuanplus.mobile.module.mine.my_order_detail.MyOrderDetailActivity.ORDER_UUID;

/**
 * Created by kk on 2018/5/24.
 */

public class MyOrderDetailPresenter implements MyOrderDetailContract.Presenter {

    private final MyOrderDetailContract.View mView;
    private String mOrderUUID;
    private int mStatus;

    @Inject
    public MyOrderDetailPresenter(MyOrderDetailContract.View mView) {

        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {

        String orderUUID = intent.getStringExtra(ORDER_UUID);
        doRequest(orderUUID);
    }


    private void doRequest(String orderUUID) {

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_ORDER_DETAIL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOrderDetailApiParameter(orderUUID).getRequestBody());

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {

            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                OrderDetailList res = new OrderDetailList(s);
                if (res.orderDetailItem != null) {
                    mOrderUUID = res.orderDetailItem.uuid;
                    mStatus = res.orderDetailItem.status;
                    mView.updatePage(true);
                    updateView(res.orderDetailItem);
                } else {
                    mView.updatePage(true);
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);

                mView.updatePage(true);
            }
        });


        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateView(OrderDetailItem item) {
//        bizType (integer, optional): 订单类型(0免单 1正常购买) ,
//                buyerUserUuid (string, optional): 买家UUID ,
//                cancelReason (string, optional): 取消原因 ,
//                cancelTime (string, optional): 取消时间 ,
//                createTime (string, optional): 订单创建时间 ,
//                logisticsType (integer, optional): 配送方式(1-商家配送、2-到店自取) ,
//                orderNum (string, optional): 订单编号 ,
//                orderPrice (integer, optional): 订单金额 ,
//                payType (integer, optional): 支付方式（1、微信，2、支付宝） ,
//        postUuid (string, optional): 商品UUID ,
//                prodCount (integer, optional): 商品数量 ,
//                prodImg (string, optional): 商品图片 ,
//                prodName (string, optional): 商品名称 ,
//                prodPrice (integer, optional): 商品单价 ,
//                recAddress (string, optional): 收件人地址 ,
//                recName (string, optional): 收件人姓名 ,
//                recPhone (string, optional): 收件人电话 ,
//                remark (string, optional): 订单备注 ,
//                sellerUserUuid (string, optional): 卖家UUID ,
//                status (integer, optional): 订单状态 ,
//                uuid (string, optional): 订单UUID ,
//                wlCode (string, optional): 物流编号

        mView.setBuyer(item.recName);
        mView.setBuyerPhone(item.recPhone);
        mView.setBuyerAddress(item.recAddress);
        mView.setProductImage(item.prodImg);
        mView.setProductTitle(item.prodName);
        mView.setProductPrice("￥" + ((float) item.prodPrice) / 100);
        mView.setBuyCount("" + item.prodCount);
        mView.setOrderPrice("￥" + ((float) item.orderPrice / 100));
        mView.setDeliveryMethodBottom(item.logisticsType == 1 ? "商家配送" : "到店自取");
        mView.setOrderID(item.orderNum);
        Date date = new Date(Long.parseLong(item.createTime));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(date);
        mView.setOrderTime(result);

        //                        免单中(1, "免单中"),
        //                        免单失败(2,“免单失败"),
        //                        待收货(3，"待收货")，
        //                        已取消(4,"已取消"),
        //                        待支付(5，"待支付"),
        //                        待发货(6,"待发货"),
        //                        待退款(7,"待退款"),
        //                        已完成(9，“已完成");|
        switch (item.status) {

            case 1:
                break;
            case 2:
                break;
            case 3:
                orderWaitForReceive(item);
                break;
            case 4:
            case 7:
                orderCanceled();
                break;
            case 5:
                break;
            case 6:
                orderImcomplete(item);
                break;

            case 9:
                orderCompleted(item);
                break;
            default:
                break;
        }
    }

    //订单取消
    private void orderCanceled() {

        mView.showOrderStatusLayout(false);
        mView.showCancelOrderLayout(true);
        mView.showCancelOrderButton(false);
    }

    //    logisticsType (integer, optional): 配送方式(1-商家配送、2-到店自取) ,
    private void orderImcomplete(OrderDetailItem item) {

        mView.showOrderStatusLayout(true);
        mView.showCancelOrderLayout(false);
        mView.showCancelOrderButton(true);
        mView.showOrderButton(true);
        mView.showNoticeText(false);

        if (2 == item.logisticsType) {
            //自取
            mView.setDeliveryMethod("到店自提");
            mView.showmEditLogistticsInfo(false);
        } else {
            //商家配送
            mView.setDeliveryMethod("商家配送");
            mView.showmEditLogistticsInfo(true);
        }
    }

    //    logisticsType (integer, optional): 配送方式(1-商家配送、2-到店自取) ,
    private void orderWaitForReceive(OrderDetailItem item) {

        mView.showOrderStatusLayout(true);
        mView.showCancelOrderLayout(false);
        mView.showCancelOrderButton(false);
        mView.showOrderButton(false);
        mView.showNoticeText(true);
        mView.showmEditLogistticsInfo(false);


        if (2 == item.logisticsType) {
            //自取
            mView.setDeliveryMethod("到店自提");
            mView.showmDeliveryLogistitcs(false);
            mView.setNoticeText("交易完成后，请提醒买家确认收货！");

        } else {
            //商家配送
            mView.setDeliveryMethod("商家配送");

            if (Utils.isStringEmpty(item.wlCode)) {
                mView.showmDeliveryLogistitcs(false);
            } else {
                mView.showmDeliveryLogistitcs(true);
                mView.setDeliveryLogistitcs(item.wlCode);
            }
            mView.setNoticeText("等待买家确认收货");

        }
    }


    private void orderCompleted(OrderDetailItem item) {
        mView.showOrderStatusLayout(true);
        mView.showCancelOrderLayout(false);
        mView.showCancelOrderButton(false);
        mView.showOrderButton(false);
        mView.showNoticeText(true);
        mView.showmEditLogistticsInfo(false);
        mView.setNoticeText("买家已确认收货，交易完成");

        if (2 == item.logisticsType) {
            //自取
            mView.setDeliveryMethod("到店自提");
            mView.showmDeliveryLogistitcs(false);


        } else {
            //商家配送
            mView.setDeliveryMethod("商家配送");

            if (Utils.isStringEmpty(item.wlCode)) {
                mView.showmDeliveryLogistitcs(false);
            } else {
                mView.showmDeliveryLogistitcs(true);
                mView.setDeliveryLogistitcs(item.wlCode);
            }

        }
    }

    public void showAccptDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mView.getDefaultContext());
        builder.setMessage("确定接单？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            acceptOrder();

        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showCancelDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mView.getDefaultContext());
        builder.setMessage("是否取消订单？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            cancelOrder();

        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void acceptOrder() {

//        if (mView.getLogisticsInfoView().getVisibility() == View.VISIBLE && Utils.isStringEmpty(mView.getLogistticsInfo())) {
//            CommonToast.getInstance("请输入物流信息").show();
//            return;
//        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SELLER_COMFIRM_ORDER);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOrderConfirmApiParameter(mOrderUUID, mView.getLogistticsInfo()).getRequestBody());

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);

        postRequest.setHttpListener(new HttpListener<String>() {

            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                ResponseData responseData = new ResponseData(s);
                if (!Utils.isStringEquals(responseData.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(responseData.mMsg).show();
                } else {
                    CommonToast.getInstance("接单成功").show();
                    doRequest(mOrderUUID);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ACCEPT_ORDER_SUCCESS, mOrderUUID));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });

        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void cancelOrder() {

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.CANCEL_ORDER);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOrderDetailApiParameter(mOrderUUID).getRequestBody());

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);

        postRequest.setHttpListener(new HttpListener<String>() {

            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                ResponseData responseData = new ResponseData(s);
                if (!Utils.isStringEquals(responseData.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(responseData.mMsg).show();


                } else {
                    CommonToast.getInstance("取消订单成功").show();
                    doRequest(mOrderUUID);
//                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, item.postUuid));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {

    }
}
