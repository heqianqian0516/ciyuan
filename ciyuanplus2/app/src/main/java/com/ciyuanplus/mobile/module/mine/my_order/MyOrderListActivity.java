package com.ciyuanplus.mobile.module.mine.my_order;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.net.bean.MyOrderItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyOrderListActivity extends MyBaseActivity implements MyOrderListContract.View, EventCenterManager.OnHandleEventListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycle_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    TitleBarView m_js_common_title;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;

    @Inject
    MyOrderListPresenter mPresenter;
    @BindView(R.id.ll_empty_view)
    LinearLayout emptyLayout;
    private Unbinder mUnbinder;
    private static final String merId = "6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);

        initView();

    }

    private void initView() {

        mUnbinder = ButterKnife.bind(this);

        DaggerMyOrderListPresenterComponent.builder()
                .myOrderListPresenterModule(new MyOrderListPresenterModule(this)).build()
                .inject(this);

        m_js_common_title.setTitle("我的订单");
        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ACCEPT_ORDER_SUCCESS, this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.doRequest(false, merId, "");

    }

    @Override
    public RecyclerView getMainList() {
        return mRecyclerView;
    }

    @Override
    public void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {

        mRefreshLayout.setEnableLoadMore(enable);
    }

    @Override
    public void finishLoadMoreAndRefresh() {

        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void updateView(List<MyOrderItem> orderItemList) {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);

    }

    @Override
    public void toH5(String url) {
        jumpToH5(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ACCEPT_ORDER_SUCCESS, this);
        mUnbinder.unbind();

    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {

        mPresenter.handleEvent(eventMessage);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        mPresenter.doRequest(true, merId, "");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        mPresenter.doRequest(false, merId, "");
    }
}
