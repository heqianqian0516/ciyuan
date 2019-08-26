package com.ciyuanplus.mobile.module.mine.change_community;

import android.app.Activity;
import android.content.Intent;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.adapter.UserCommunityAdapter;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.utils.Constants;

import javax.inject.Inject;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Alen on 2017/12/11.
 */

public class ChangeCommunityPresenter implements ChangeCommunityContract.Presenter {
    public CommunityItem mDefaultCommunity = new CommunityItem();
    private CommunityItem[] mCommunityItems;
    private final ChangeCommunityContract.View mView;
    private UserCommunityAdapter mAdapter;

    @Inject
    public ChangeCommunityPresenter(ChangeCommunityContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {
        CommunityManager.getInstance().getCommunityListFromNet();// 获取一下小区列表，万一是空对象先初始化请求小区列表

        mAdapter = new UserCommunityAdapter((Activity) mView, mCommunityItems, (v) -> {
            int postion = mView.getListView().getChildLayoutPosition(v);
            mDefaultCommunity = mAdapter.getItem(postion);
            CommunityManager.getInstance().setDefaultCommunity(mDefaultCommunity);
            SharedPreferencesManager.putString("MyAddress", "address", mDefaultCommunity.commName);

            Intent intent = new Intent("android.intent.action.refresh");
            intent.putExtra("refresh", "lang");
            intent.putExtra("keyi", mDefaultCommunity.uuid);
            LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent);

            Intent intent2 = new Intent("android.intent.action.muhomerefresh");
            intent2.putExtra("refresh", "lang");
            intent2.putExtra("keyi", mDefaultCommunity.uuid);
            LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent2);
            ((Activity) mView).finish();
        });
        mView.getListView().setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH) {
            // 刷新小区列表
            mCommunityItems = CommunityManager.getInstance().getmCommunityItems();
            mDefaultCommunity.uuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
            mDefaultCommunity.commName = UserInfoData.getInstance().getUserInfoItem().currentCommunityName;

            mAdapter.setCommunityItems(mCommunityItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void detachView() {
    }
}
