package com.ciyuanplus.mobile.module.mine.my_order;

import com.ciyuanplus.mobile.adapter.MyOrderAdapter;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.bean.MyOrderItem;
import com.ciyuanplus.mobile.net.parameter.RequestOrderListApiParameter;
import com.ciyuanplus.mobile.net.response.MyOrderListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kk on 2018/5/24.
 */

public class MyOrderListPresenter implements MyOrderListContract.Presenter {

    private final MyOrderListContract.View mView;
    private MyOrderAdapter myOrderAdapter;
    private final List<MyOrderItem> myOrderItemList = new ArrayList<>();
    private int pager = 0;
    private static final int pageSize = 20;

    @Inject
    public MyOrderListPresenter(MyOrderListContract.View mView) {
        this.mView = mView;

//        doRequest(false);
        myOrderAdapter = new MyOrderAdapter(mView.getDefaultContext(), myOrderItemList, v -> {
            int position = mView.getMainList().getChildAdapterPosition(v);
            clickItem(position);

        });
        mView.getMainList().setAdapter(myOrderAdapter);
    }

    @Override
    public void doRequest(boolean reset, String merId, String status) {

        if (reset) {
            myOrderItemList.clear();
            pager = 0;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_ORDER_LIST);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOrderListApiParameter(merId, status, String.valueOf(pager), String.valueOf(pageSize)).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);

        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {

            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.finishLoadMoreAndRefresh();

                MyOrderListResponse myOrderListResponse = new MyOrderListResponse(s);
                ArrayList<MyOrderItem> orderItemList = myOrderListResponse.myOrderItemList;

                if (orderItemList != null) {
                    if (orderItemList.size() > 0) {
                        myOrderItemList.addAll(orderItemList);
                        mView.setLoadMoreEnable(orderItemList.size() >= pageSize);
                        pager++;
                    }
                }

                myOrderAdapter.notifyDataSetChanged();

                if (myOrderItemList.size() > 0) {
                    mView.updateView(myOrderItemList);
                } else {
                    mView.showEmptyView();
                }

            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.finishLoadMoreAndRefresh();

            }
        });

        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void clickItem(int position) {



        String userUUid = UserInfoData.getInstance().getUserInfoItem().uuid;
        String authToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");

        String url = ApiContant.WEB_DETAIL_VIEW_URL + "cyplus-share/order.html?userUuid=" + userUUid + "&authToken=" + authToken + "&orderId=" + myOrderItemList.get(position).getId();
        mView.toH5(url);
    }

    @Override
    public void detachView() {

    }

    public void handleEvent(EventCenterManager.EventMessage eventMessage) {

        if (Constants.EVENT_MESSAGE_ACCEPT_ORDER_SUCCESS == eventMessage.mEvent) {

            String mOrderUUID = (String) eventMessage.mObject;
            for (int i = 0; i < myOrderItemList.size(); i++) {

//                if (Utils.isStringEquals(mOrderUUID, myOrderItemList.get(i).uuid)) {
//                    myOrderItemList.get(i).prodOrderStatus = 3;
//                    myOrderAdapter.notifyDataSetChanged();
//                }


            }
        }
    }
}
