package com.ciyuanplus.mobile.module.mine.my_publish.MineStuff;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/26.
 */

public class MineStuffFragment extends MyFragment implements EventCenterManager.OnHandleEventListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.m_mine_stuff_list)
    IRecyclerView mMineStuffList;
    @BindView(R.id.m_mine_stuff_null_lp)
    LinearLayout mMineStuffNullLp;
    private LoadMoreFooterView loadMoreFooterView;

    private int mStuffNextPage = 0;
    private StuffAdapter mAdapter;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_stuff, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mMineStuffList.setLayoutManager(gridLayoutManager);
        mMineStuffList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
                outRect.right = Utils.dip2px(10);
            }
        });
        loadMoreFooterView = (LoadMoreFooterView) mMineStuffList.getLoadMoreFooterView();
        mMineStuffList.setOnLoadMoreListener(this);
        mMineStuffList.setOnRefreshListener(this);

        mAdapter = new StuffAdapter(getActivity(), mStuffList, (v)-> {
            int postion = mMineStuffList.getChildLayoutPosition(v) - 2;
            FreshNewItem item = mAdapter.getItem(postion);
            Intent intent = new Intent(getActivity(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            getActivity().startActivity(intent);
        });
        mMineStuffList.setIAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        requestStuffList();
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
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
        }else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST) {
            mStuffNextPage = 0;
            requestStuffList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
   public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            requestStuffList();
        }
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mStuffNextPage = 0;
        requestStuffList();
    }

    private void requestStuffList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_MY_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestMineStuffListApiParameter(UserInfoData.getInstance().getUserInfoItem().uuid, AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mStuffNextPage + "", PAGE_SIZE + "",UserInfoData.getInstance().getUserInfoItem().uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                stopLoadMoreAndRefresh();

                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.length > 0) {
                        if (mStuffNextPage == 0) mStuffList.clear();

                        Collections.addAll(mStuffList, response1.stuffListItem.list);
                        mStuffNextPage++;
                        mAdapter.notifyDataSetChanged();
                    }
                    updateNullView(mStuffList.size());
                } else {
                    CommonToast.getInstance(response1.mMsg).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                stopLoadMoreAndRefresh();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void stopLoadMoreAndRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mMineStuffList.setRefreshing(false);
    }

    private void updateNullView(int size) {
        if(size > 0){
            mMineStuffList.setVisibility(View.VISIBLE);
            mMineStuffNullLp.setVisibility(View.GONE);
        } else {
            mMineStuffList.setVisibility(View.GONE);
            mMineStuffNullLp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST, this);

        super.onDestroy();
    }
}
