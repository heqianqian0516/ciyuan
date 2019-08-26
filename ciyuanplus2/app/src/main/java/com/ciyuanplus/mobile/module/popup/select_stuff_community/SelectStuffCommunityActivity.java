package com.ciyuanplus.mobile.module.popup.select_stuff_community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.UserCommunityAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.google.gson.Gson;

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

public class SelectStuffCommunityActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener {

    public String selectCommunityId;
    @BindView(R.id.m_select_stuff_community_title)
    TextView mSelectStuffCommunityTitle;
    @BindView(R.id.m_select_stuff_community_close_image)
    ImageView mSelectStuffCommunityCloseImage;
    @BindView(R.id.m_select_stuff_community_list_view)
    RecyclerView mSelectStuffCommunityListView;
    private CommunityItem[] mCommunityItems;
    private UserCommunityAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_stuff_community);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        selectCommunityId = getIntent().getStringExtra(Constants.INTENT_COMMUNITY_ID);
        this.initView();
        this.initData();
    }

    private void initData() {// 初始化数据，
        CommunityManager.getInstance().getCommunityListFromNet();// 获取一下小区列表，万一是空对象先初始化请求小区列表
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mSelectStuffCommunityListView.setLayoutManager(linearLayoutManager);
        mAdapter = new UserCommunityAdapter(this, mCommunityItems, (v) -> {
            int postion = mSelectStuffCommunityListView.getChildLayoutPosition(v);
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_COMMUNITY_ITEM, new Gson().toJson(mAdapter.getItem(postion)));
            setResult(RESULT_OK, intent);
            finish();
        });
        mSelectStuffCommunityListView.setAdapter(mAdapter);

        // 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);//

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH) {
            // 刷新小区列表
            mCommunityItems = CommunityManager.getInstance().getmCommunityItems();

            mAdapter.setCommunityItems(mCommunityItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.m_select_stuff_community_close_image)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        finish();
    }
}
