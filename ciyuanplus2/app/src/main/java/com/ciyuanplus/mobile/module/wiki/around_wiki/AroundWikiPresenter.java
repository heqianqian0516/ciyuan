package com.ciyuanplus.mobile.module.wiki.around_wiki;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.WikiAdapter;
import com.ciyuanplus.mobile.adapter.WikiTypeAdapter;
import com.ciyuanplus.mobile.manager.WikiTypeManager;
import com.ciyuanplus.mobile.module.wiki.wiki_position.WikiPositionActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.net.bean.WikiTypeItem;
import com.ciyuanplus.mobile.net.parameter.RequestAroundWikiApiParameter;
import com.ciyuanplus.mobile.net.response.RequestAroundWikiResponse;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
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

public class AroundWikiPresenter implements AroundWikiContract.Presenter {
    public WikiTypeItem mWikiType;
    private int mNextPage = 0;
    private final int PAGE_SIZE = 20;
    private final AroundWikiContract.View mView;
    private final ArrayList<WikiItem> mList = new ArrayList<>();
    private WikiAdapter mAdapter;
    private WikiTypeAdapter mWikiTypeAdapter;

    @Inject
    public AroundWikiPresenter(AroundWikiContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {//初始化数据
        WikiTypeItem[] types = WikiTypeManager.getInstance().getTypeInfo();
        mWikiType = types[0];// 默认是全部
        mWikiTypeAdapter = new WikiTypeAdapter((AroundWikiActivity) mView.getDefaultContext(), types, (v) -> {
            int position = mView.getTopWikiTypeGrid().getChildAdapterPosition(v);
            StatisticsManager.onEventInfo("AroundWikiActivity", "typeItemClick", mWikiTypeAdapter.getItem(position).id);
            mWikiType = mWikiTypeAdapter.getItem(position);
            mWikiTypeAdapter.notifyDataSetChanged();
            mList.clear();
            // 切换之后  立刻刷新数据
            requestWikiList(true);
        });
        mView.getTopWikiTypeGrid().setAdapter(mWikiTypeAdapter);


        mAdapter = new WikiAdapter((Activity) mView.getDefaultContext(), mList, (view) -> {
            int postion = mView.getWikiList().getChildAdapterPosition(view) - 2;
            Intent intent = new Intent(mView.getDefaultContext(), WikiPositionActivity.class);
            intent.putExtra(Constants.INTENT_POSITION_ITEM, mAdapter.getItem(postion));
            mView.getDefaultContext().startActivity(intent);
        });
        mView.getWikiList().setIAdapter(mAdapter);

        requestWikiList(true);// 进来就需要直接刷新数据
    }

    @Override
    public void detachView() {


    }

    @Override
    public void requestWikiList(boolean b) {
        if (b) mNextPage = 0;// 判断是否需要清除之前数据
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_COMMUNITY_WIKI_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestAroundWikiApiParameter(mNextPage + "", PAGE_SIZE + "", mWikiType == null ? "" : mWikiType.id).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.stopRefreshAndLoadMore();
                RequestAroundWikiResponse response1 = new RequestAroundWikiResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (mNextPage == 0) mList.clear();
                    if (response1.wikiListItem.list != null && response1.wikiListItem.list.length > 0) {
                        Collections.addAll(mList, response1.wikiListItem.list);
                        mNextPage++;
                    } else {
                        mView.setLoadStatueTheEnd();
                    }
                    mAdapter.notifyDataSetChanged();
                    mView.updateView(mList.size() > 0);
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.stopRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

}
