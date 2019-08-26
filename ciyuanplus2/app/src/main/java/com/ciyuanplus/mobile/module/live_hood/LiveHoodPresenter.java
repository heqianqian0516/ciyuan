package com.ciyuanplus.mobile.module.live_hood;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.LiveHoodAdapter;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.LiveHoodItem;
import com.ciyuanplus.mobile.net.parameter.RequestLiveHoodApiParameter;
import com.ciyuanplus.mobile.net.response.RequestLiveHoodResponse;
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

public class LiveHoodPresenter implements LiveHoodContract.Presenter {

    private final LiveHoodContract.View mView;
    private final ArrayList<LiveHoodItem> mLiveHoodList = new ArrayList<>();
    private LiveHoodAdapter mLiveHoodAdapter;

    @Inject
    public LiveHoodPresenter(LiveHoodContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {
        mLiveHoodAdapter = new LiveHoodAdapter((Activity) mView, mLiveHoodList, (v) -> {
            int position = mView.getRecyclerView().getChildAdapterPosition(v);
            if (mLiveHoodAdapter.getItem(position).allowedUserState == 1 && !LoginStateManager.isLogin()) {
                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
                mView.getDefaultContext().startActivity(intent);
                return;
            }
            Intent intent = new Intent(mView.getDefaultContext(), JsWebViewActivity.class);
            intent.putExtra(Constants.INTENT_OPEN_URL, mLiveHoodAdapter.getItem(position).url);
            mView.getDefaultContext().startActivity(intent);
        });
        mView.getRecyclerView().setAdapter(mLiveHoodAdapter);
        requestLiveHoodList();

    }

    private void requestLiveHoodList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_LIVE_HOOD_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        int nextPage = 0;
        postRequest.setHttpBody(new RequestLiveHoodApiParameter("" + nextPage, "" + PAGE_SIZE).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestLiveHoodResponse response1 = new RequestLiveHoodResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    //民生一定是大于4个的  这里跟后台确认了
                    if (response1.liveHoodListItem.list != null) {
                        mLiveHoodList.clear();
                        Collections.addAll(mLiveHoodList, response1.liveHoodListItem.list);
                        mLiveHoodAdapter.notifyDataSetChanged();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
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
