package com.ciyuanplus.mobile.module.found.market;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.PopupMarkteSortAdapter;
import com.ciyuanplus.mobile.adapter.StuffAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.RequestWorldStuffApiParameter;
import com.ciyuanplus.mobile.net.response.RequestStuffListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class MarketPresenter implements MarketContract.Presenter {
    public String mStatus = "9";
    public String mOrder = "1";
    public int mOption;
    public PopupMarkteSortAdapter mSortAdapter;
    private final MarketContract.View mView;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();
    private int mNextPage = 0;
    private StuffAdapter mAdapter;

    @Inject
    public MarketPresenter(MarketContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void initData() {
        mAdapter = new StuffAdapter((Activity) mView, mStuffList, (v) -> {
            int postion = mView.getRecyclerView().getChildLayoutPosition(v) - 2;
            FreshNewItem item = mAdapter.getItem(postion);
            Intent intent = new Intent(mView.getDefaultContext(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
            mView.getDefaultContext().startActivity(intent);
        });
        mView.getRecyclerView().setIAdapter(mAdapter);
        requestStuffList(true);
    }

    @Override
    public void requestStuffList(boolean reset) {
        if (reset) mNextPage = 0;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_WORLD_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestWorldStuffApiParameter(AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mOrder, mStatus, mNextPage + "", PAGE_SIZE + "").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.stopLoadMoreAndRefresh();

                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (mNextPage == 0) mStuffList.clear();
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.length > 0) {
                        Collections.addAll(mStuffList, response1.stuffListItem.list);
                        mNextPage++;
                    }
                    mAdapter.notifyDataSetChanged();
                    mView.updateView(mStuffList.size());
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.stopLoadMoreAndRefresh();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST) {
            requestStuffList(true);
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.remove(i);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void detachView() {
    }
}
