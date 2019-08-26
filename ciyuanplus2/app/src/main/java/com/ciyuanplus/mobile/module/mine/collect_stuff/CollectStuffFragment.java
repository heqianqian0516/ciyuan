package com.ciyuanplus.mobile.module.mine.collect_stuff;

import android.content.Context;
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
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/28.
 */

public class CollectStuffFragment extends MyFragment implements CollectStuffContract.View, EventCenterManager.OnHandleEventListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.m_collect_stuff_list)
    IRecyclerView mCollectStuffList;
    @BindView(R.id.m_collect_stuff_null_lp)
    LinearLayout mCollectStuffNullLp;
    @Inject

    CollectStuffPresenter mPresenter;
    private LoadMoreFooterView loadMoreFooterView;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_stuff, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        DaggerCollectStuffPresenterComponent.builder()
                .collectStuffPresenterModule(new CollectStuffPresenterModule(this)).build().inject(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mCollectStuffList.setLayoutManager(gridLayoutManager);
        mCollectStuffList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
                outRect.right = Utils.dip2px(10);
            }
        });
        loadMoreFooterView = (LoadMoreFooterView) mCollectStuffList.getLoadMoreFooterView();
        mCollectStuffList.setOnLoadMoreListener(this);
        mCollectStuffList.setOnRefreshListener(this);

        mPresenter.initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        mPresenter.requestStuffList(true);
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mPresenter.requestStuffList(true);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.requestStuffList(false);
        }
    }

    @Override
    public IRecyclerView getRecyclerView() {
        return mCollectStuffList;
    }

    @Override
    public void stopLoadMoreAndRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mCollectStuffList.setRefreshing(false);
    }


    @Override
    public void updateView(int size) {//
        if (size > 0) {
            mCollectStuffList.setVisibility(View.VISIBLE);
            mCollectStuffNullLp.setVisibility(View.GONE);
        } else {
            mCollectStuffList.setVisibility(View.GONE);
            mCollectStuffNullLp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);

        super.onDestroy();
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        mPresenter.handleEvent(eventMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public Context getDefaultContext() {
        return getActivity();
    }
}
