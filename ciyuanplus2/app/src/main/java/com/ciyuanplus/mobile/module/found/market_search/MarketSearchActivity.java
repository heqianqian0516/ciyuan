package com.ciyuanplus.mobile.module.found.market_search;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.ClearEditText;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/21.
 */

public class MarketSearchActivity extends MyBaseActivity implements MarketSearchContract.View, OnLoadMoreListener, OnRefreshListener {
    @Inject
    public MarketSearchPresenter mPresenter;
    @BindView(R.id.m_search_market_cancel)
    TextView mSearchMarketCancel;
    @BindView(R.id.m_search_market_search_edit)
    ClearEditText mSearchMarketSearchEdit;
    @BindView(R.id.m_search_market_top_lp)
    RelativeLayout mSearchMarketTopLp;
    @BindView(R.id.m_search_market_null_lp)
    LinearLayout mSearchMarketNullLp;
    @BindView(R.id.m_search_market_scroll_grid_lp)
    IRecyclerView mGridView;
    private LoadMoreFooterView loadMoreFooterView;

    //显示虚拟键盘
    private static void ShowKeyboard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_market);
        this.initView();
        StatisticsManager.onEventInfo("search_stuff", "search_stuff_page_load");
        mPresenter.initData();
        ShowKeyboard(mSearchMarketSearchEdit);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerMarketSearchPresenterComponent.builder()
                .marketSearchPresenterModule(new MarketSearchPresenterModule(this)).build().inject(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDefaultContext(), 2);
        mGridView.setLayoutManager(gridLayoutManager);
        loadMoreFooterView = (LoadMoreFooterView) mGridView.getLoadMoreFooterView();
        mGridView.setOnLoadMoreListener(this);
        mGridView.setOnRefreshListener(this);
        mGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
                outRect.right = Utils.dip2px(10);
            }
        });

        mSearchMarketSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                HideKeyboard(mSearchMarketSearchEdit);
                String search = mSearchMarketSearchEdit.getText().toString();
                if (!Utils.isStringEmpty(search)) {
                    mPresenter.mSearch = search;
                    mPresenter.requestStuffList(true);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void stopLoadMoreAndRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mGridView.setRefreshing(false);
    }

    @Override
    public IRecyclerView getRecyclerView() {
        return mGridView;
    }

    @Override
    public void updateView(int size) {
        if (size > 0) {
            mGridView.setVisibility(View.VISIBLE);
            mSearchMarketNullLp.setVisibility(View.GONE);
        } else {
            mGridView.setVisibility(View.GONE);
            mSearchMarketNullLp.setVisibility(View.VISIBLE);
        }
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

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    @OnClick(R.id.m_search_market_cancel)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        onBackPressed();
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
