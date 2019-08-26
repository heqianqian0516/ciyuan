package com.ciyuanplus.mobile.module.start_forum.select_post_location;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/4.
 * <p>
 * 选择位置页面
 */

public class SelectPostLocationActivity extends MyBaseActivity implements SelectPostLocationContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.m_select_post_location_search_edit)
    ClearEditText mSelectPostLocationSearchEdit;
    @BindView(R.id.m_select_post_location_list)
    IRecyclerView mSelectPostLocationList;
    @BindView(R.id.m_select_post_location_common_title)
    CommonTitleBar commonBar;

    @Inject
 SelectPostLocationPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_post_location);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        this.initView();

        mPresenter.initData(getIntent());

        mPresenter.doSearchQuery("");
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerSelectPostLocationPresenterComponent.builder().
                selectPostLocationPresenterModule(new SelectPostLocationPresenterModule(this)).build().inject(this);
        commonBar.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        commonBar.setCenterText("选择位置");

        mSelectPostLocationSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_COMMUNITY,
                        StatisticsConstant.OP_SEARCH_COMMUNITY_FINISH_CLICK);
                HideKeyboard(mSelectPostLocationSearchEdit);
                mPresenter.doSearchQuery(mSelectPostLocationSearchEdit.getText().toString());
                return true;
            }
            return false;
        });

        LoadMoreFooterView loadMoreFooterView = (LoadMoreFooterView) mSelectPostLocationList.getLoadMoreFooterView();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);//   LinearLayoutManager不能共用 真是坑爹
        mSelectPostLocationList.setLayoutManager(linearLayoutManager1);
        mSelectPostLocationList.setOnRefreshListener(this);
        mSelectPostLocationList.setOnLoadMoreListener(this);

    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public IRecyclerView getListView() {
        return mSelectPostLocationList;
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void onRefresh() {

    }

    @Override
   public void onLoadMore() {
        mPresenter.doSearchQuery(mSelectPostLocationSearchEdit.getText().toString());
    }
}
