package com.ciyuanplus.mobile.module.wiki.around_wiki;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.net.bean.WikiTypeItem;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/9/19.
 * 周边百科页面
 * <p>
 * 顶部栏目用GridView实现
 */

public class AroundWikiActivity extends MyBaseActivity implements AroundWikiContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.m_around_wiki_back_image)
    ImageView mAroundWikiBackImage;
    @BindView(R.id.m_around_wiki_grid)
    RecyclerView mAroundWikiGrid;
    @BindView(R.id.m_around_wiki_list)
    IRecyclerView mAroundWikiList;
    @BindView(R.id.m_around_wiki_null_lp)
    LinearLayout mAroundWikiNullLp;
    @Inject

    AroundWikiPresenter mPresenter;
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_around_wiki);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData();//初始化数据
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerAroundWikiPresenterComponent.builder()
                .aroundWikiPresenterModule(new AroundWikiPresenterModule(this)).build().inject(this);

        // 这是顶部的RecyclerView 属性
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAroundWikiGrid.setLayoutManager(linearLayoutManager);

        loadMoreFooterView = (LoadMoreFooterView) mAroundWikiList.getLoadMoreFooterView();

        // 这是列表RecyclerView 属性
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);//   LinearLayoutManager不能共用 真是坑爹
        mAroundWikiList.setLayoutManager(linearLayoutManager1);
        mAroundWikiList.setOnRefreshListener(this);
        mAroundWikiList.setOnLoadMoreListener(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.requestWikiList(true);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.requestWikiList(false);
        }
    }

    @Override
    public RecyclerView getTopWikiTypeGrid() {
        return mAroundWikiGrid;
    }

    @Override
    public IRecyclerView getWikiList() {
        return mAroundWikiList;
    }

    @Override
    public void stopRefreshAndLoadMore() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mAroundWikiList.setRefreshing(false);
    }

    @Override
    public void setLoadStatueTheEnd() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
    }

    @Override
    public void updateView(boolean isNull) {
        if (isNull) {
            mAroundWikiNullLp.setVisibility(View.GONE);
            mAroundWikiList.setVisibility(View.VISIBLE);
        } else {
            mAroundWikiNullLp.setVisibility(View.VISIBLE);
            mAroundWikiList.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.m_around_wiki_back_image)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_around_wiki_back_image) {
            onBackPressed();
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    public WikiTypeItem getWikiType() {
        return mPresenter.mWikiType;
    }
}
