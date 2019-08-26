package com.ciyuanplus.mobile.module.found.market_search;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.StuffAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.SearchStuffListApiParameter;
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

public class MarketSearchPresenter implements MarketSearchContract.Presenter {
    public String mSearch;
    private final MarketSearchContract.View mView;
    private int mNextPage = 0;
    private StuffAdapter mAdapter;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();

    @Inject
    public MarketSearchPresenter(MarketSearchContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {
        mAdapter = new StuffAdapter((Activity) mView, mStuffList, (v) -> {
            int postion = mView.getRecyclerView().getChildLayoutPosition(v) - 2;
            FreshNewItem item = mAdapter.getItem(postion);
            Intent intent = new Intent(mView.getDefaultContext(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);
        });
        mView.getRecyclerView().setIAdapter(mAdapter);
    }

    @Override
    public void requestStuffList(boolean reset) {
        if (reset) mNextPage = 0;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SEARCH_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new SearchStuffListApiParameter(AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mSearch, mNextPage + "", PAGE_SIZE + "").getRequestBody());
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
    public void detachView() {
    }
}
