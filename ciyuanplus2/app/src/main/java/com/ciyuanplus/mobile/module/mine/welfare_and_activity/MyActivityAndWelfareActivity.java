package com.ciyuanplus.mobile.module.mine.welfare_and_activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.pulltorefresh.XRecyclerView;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyActivityAndWelfareActivity extends MyBaseActivity implements MyActivityAndWelfareContract.View {

//    @BindView(R.id.smartRefreshLayout)
//    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.title_bar)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.rcl_list)
    XRecyclerView recyclerView;
    @BindView(R.id.m_mine_welfare_null_lp)
    LinearLayout emptyView;

    @Inject
    MyActivityAndWelfarePresenter myActivityAndWelfarePresenter;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_and_welfare);

        initView();

        DaggerMyActivityAndWelfarePresenterComponent.builder()
                .myActivityAndWelfarePresenterModule(new MyActivityAndWelfarePresenterModule(this))
                .build().inject(this);
    }

    private void initView() {

        mUnbinder = ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_js_common_title.setCenterText("我的活动");
        m_js_common_title.hideRightImage();
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public XRecyclerView getMainList() {
        return recyclerView;
    }

    @Override
    public void updatePage(boolean haveData) {

        recyclerView.setVisibility(haveData ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(haveData ? View.GONE : View.VISIBLE);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
