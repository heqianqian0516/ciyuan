package com.ciyuanplus.mobile.module.forum_detail.want_list;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/9.
 */

public class WantListActivity extends MyBaseActivity implements WantListContract.View {
    @BindView(R.id.m_want_list_recycler_view)
    RecyclerView mWantListRecyclerView;
    @BindView(R.id.m_want_list_common_title)
    CommonTitleBar mCommonBar;
    @BindView(R.id.m_want_list_null_lp)
    LinearLayout mWantListNullLp;
    @Inject
 WantListPresenter mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_want_list);

        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerWantListPresenterComponent.builder().
                wantListPresenterModule(new WantListPresenterModule(this)).build().inject(this);

        mCommonBar.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDefaultContext(), 4);
        mWantListRecyclerView.setLayoutManager(gridLayoutManager);

        mPresenter.initData(getIntent());
    }

    @Override
    public void setCenterTitle(String title) {
        mCommonBar.setCenterText(title);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public RecyclerView getGridView() {
        return mWantListRecyclerView;
    }

    @Override
    public void updateNullView(boolean visible) {
        mWantListNullLp.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
