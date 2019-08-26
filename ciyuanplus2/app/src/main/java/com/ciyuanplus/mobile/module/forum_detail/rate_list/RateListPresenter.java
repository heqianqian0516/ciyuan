package com.ciyuanplus.mobile.module.forum_detail.rate_list;

import android.content.Intent;

import com.ciyuanplus.mobile.adapter.AllRateAdapater;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.PlaceDetailItem;
import com.ciyuanplus.mobile.net.bean.RateItem;
import com.ciyuanplus.mobile.net.parameter.GetAllRateListApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetPlaceDetailApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetUserPlaceCommentApiParameter;
import com.ciyuanplus.mobile.net.response.GetAllRateListResponse;
import com.ciyuanplus.mobile.net.response.GetPlaceDetailResponse;
import com.ciyuanplus.mobile.net.response.GetUserPlaceCommentResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class RateListPresenter implements RateListContract.Presenter {
    private final RateListContract.View mView;

    private String mWikiId;
    private int mNextPage = 0;

    public PlaceDetailItem mPlaceDetailItem;

    private final ArrayList<RateItem> mRatesList = new ArrayList<>();
    private AllRateAdapater mAllRateAdapter;
    public RateItem mMineComment;
    @Inject
    public RateListPresenter(RateListContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent) {
        FreshNewItem item = (FreshNewItem) intent.getSerializableExtra(Constants.INTENT_NEWS_ITEM);
        mWikiId = intent.getStringExtra(Constants.INTENT_WIKI_ID);

        mAllRateAdapter = new AllRateAdapater(mView.getDefaultContext(), mRatesList, null);
        mView.getGridView().setIAdapter(mAllRateAdapter);

        if(item == null){// 如果是从wiki那边调过来的，需要先请求详情
            mView.setTitleString("一米点评");
            getPlaceDetail();
        } else {
            mPlaceDetailItem = new PlaceDetailItem();
            if(!Utils.isStringEmpty(item.imgs))mPlaceDetailItem.indexImg = item.imgs.split(",")[0];
            mPlaceDetailItem.score = item.placeScore;
            mPlaceDetailItem.name = item.placeName;
            if(item.ratePhotos != null)mPlaceDetailItem.rateCount = item.ratePhotos.length;
            mPlaceDetailItem.id = item.placeId;
            mPlaceDetailItem.address = item.placeAddress;
            mPlaceDetailItem.longitude = item.longitude;
            mPlaceDetailItem.latitude = item.latitude;
            mView.setTitleString("点评");//mItem.bizType == FreshNewItem.FRESH_ITEM_FOOD ? "吃过" : "体验过"
            mView.updateView();
            getUserPlaceComment();
            getRateList();
        }
    }

    @Override
    public void getRateList(boolean reset){
        if(reset) mNextPage = 0;
        getRateList();
    }

    private void getRateList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_ALL_RATE_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new GetAllRateListApiParameter(mPlaceDetailItem.id, mNextPage + "", PAGE_SIZE + "").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.stopRereshAndLoad();
                GetAllRateListResponse response1 = new GetAllRateListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if(mNextPage == 0)mRatesList.clear();
                    for(int i = 0 ;  i < response1.allRateListItem.data.length; i++){
                        if(!Utils.isStringEquals(response1.allRateListItem.data[i].userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                            mRatesList.add(response1.allRateListItem.data[i]);
                    }
                    mAllRateAdapter.notifyDataSetChanged();
                }else {
                    CommonToast.getInstance(response1.mMsg).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.stopRereshAndLoad();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void getPlaceDetail() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_PLACE_DETAIL_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetPlaceDetailApiParameter(mWikiId).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetPlaceDetailResponse response1 = new GetPlaceDetailResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mPlaceDetailItem = response1.placeDetail;
                    mView.updateView();
                    getUserPlaceComment();
                    getRateList();
                }else {
                    CommonToast.getInstance(response1.mMsg).show();
                }
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
    private void getUserPlaceComment() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_USER_PLACE_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetUserPlaceCommentApiParameter(mPlaceDetailItem.id).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetUserPlaceCommentResponse response1 = new GetUserPlaceCommentResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mMineComment = response1.comment;
                    mView.updateMineCommentView();
                }else {
                    CommonToast.getInstance(response1.mMsg).show();
                }
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
