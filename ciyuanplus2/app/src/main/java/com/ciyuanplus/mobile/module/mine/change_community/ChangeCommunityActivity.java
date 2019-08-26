package com.ciyuanplus.mobile.module.mine.change_community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.utils.Constants;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/9.
 * <p>
 * 切换小区页面
 */

public class ChangeCommunityActivity extends MyBaseActivity implements ChangeCommunityContract.View, EventCenterManager.OnHandleEventListener {

    @Inject
    public ChangeCommunityPresenter mPresenter;
    @BindView(R.id.m_change_community_title)
    TextView mChangeCommunityTitle;
    @BindView(R.id.m_change_community_close_image)
    ImageView mChangeCommunityCloseImage;
    @BindView(R.id.m_change_community_list_view)
    RecyclerView mChangeCommunityListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_community);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerChangeCommunityPresenterComponent.builder()
                .changeCommunityPresenterModule(new ChangeCommunityPresenterModule(this)).build().inject(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mChangeCommunityListView.setLayoutManager(linearLayoutManager);
        // 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);//
    }

    @Override
    public RecyclerView getListView() {
        return mChangeCommunityListView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        mPresenter.handleEvent(eventMessage);
    }

    @OnClick(R.id.m_change_community_close_image)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        finish();

    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
