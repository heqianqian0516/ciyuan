package com.ciyuanplus.mobile.module.forum_detail.want_list;

import android.content.Intent;

import com.ciyuanplus.mobile.adapter.AllWantsAdapater;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.WantsItem;
import com.ciyuanplus.mobile.net.parameter.GetAllWantListApiParameter;
import com.ciyuanplus.mobile.net.response.GetAllWantListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class WantListPresenter implements WantListContract.Presenter {
    private final WantListContract.View mView;

    private String postUuid;
    private String placeId;

    private final ArrayList<WantsItem> mWantsList = new ArrayList<>();
    private AllWantsAdapater mAllWantAdapter;
    @Inject
    public WantListPresenter(WantListContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent) {
        postUuid = intent.getStringExtra(Constants.INTENT_POST_ID);
        placeId = intent.getStringExtra(Constants.INTENT_PLACE_ID);
        int bizType = intent.getIntExtra(Constants.INTENT_BIZE_TYPE, 0);
        mView.setCenterTitle("收藏");

        mAllWantAdapter = new AllWantsAdapater(mView.getDefaultContext(), mWantsList, v -> {
            int postion = mView.getGridView().getChildLayoutPosition(v);
            WantsItem item = mAllWantAdapter.getItem(postion);
            //   是自己发布的帖子，不允许进入他人页面
            if (Utils.isStringEquals(item.uuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                return;
            Intent intent1 = new Intent(mView.getDefaultContext(), OthersActivity.class);
            intent1.putExtra(Constants.INTENT_USER_ID, item.uuid);
            mView.getDefaultContext().startActivity(intent1);
        });
        mView.getGridView().setAdapter(mAllWantAdapter);

        getWantList();
    }

    @Override
    public void getWantList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_ALL_WISH_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetAllWantListApiParameter(postUuid, placeId).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetAllWantListResponse response1 = new GetAllWantListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mWantsList.clear();
                    Collections.addAll(mWantsList, response1.allWantListItem.data);
                    mAllWantAdapter.notifyDataSetChanged();
                }else {
                    CommonToast.getInstance(response1.mMsg).show();
                }
                mView.updateNullView(mWantsList.size() == 0);
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
