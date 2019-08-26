package com.ciyuanplus.mobile.module.live_hood;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/11/27.
 * <p>
 * 便民服务 页面
 */

public class LiveHoodActivity extends MyBaseActivity implements LiveHoodContract.View {
    @BindView(R.id.m_live_hood_grid)
    RecyclerView mLiveHoodGrid;
    @BindView(R.id.m_live_hood_common_title)
    CommonTitleBar m_js_common_title;

    @Inject

    LiveHoodPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_live_hood);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData();
    }

    private void initView() {
        ButterKnife.bind(this);
        DaggerLiveHoodPresenterComponent.builder()
                .liveHoodPresenterModule(new LiveHoodPresenterModule(this)).build().inject(this);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("便民服务");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mLiveHoodGrid.setLayoutManager(gridLayoutManager);
        mLiveHoodGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
            }
        });
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mLiveHoodGrid;
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
