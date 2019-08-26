package com.ciyuanplus.mobile.module.mine.collect_stuff;

import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.StuffAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.RequestMineStuffListApiParameter;
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

import androidx.fragment.app.Fragment;

/**
 * Created by Alen on 2017/12/11.
 */

public class CollectStuffPresenter implements CollectStuffContract.Presenter {
    private final CollectStuffContract.View mView;

    private int mNextPage = 0;

    private StuffAdapter mAdapter;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();

    @Inject
    public CollectStuffPresenter(CollectStuffContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {
        mAdapter = new StuffAdapter(((Fragment) mView).getActivity(), mStuffList, (v) -> {
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
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COLLECT_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestMineStuffListApiParameter(UserInfoData.getInstance().getUserInfoItem().uuid, AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mNextPage + "", PAGE_SIZE + "",UserInfoData.getInstance().getUserInfoItem().uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.stopLoadMoreAndRefresh();
                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.length > 0) {
                        if (mNextPage == 0) mStuffList.clear();
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

    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM) {
            FreshNewItem item = (FreshNewItem) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(item.postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.set(i, item);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.remove(i);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.get(i).browseCount++;
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }
}
