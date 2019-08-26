package com.ciyuanplus.mobile.module.settings.message_setting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.NoticeSettingManager;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/24.
 */

public class MessageSettingActivity extends MyBaseActivity implements MessageSettingContract.View, EventCenterManager.OnHandleEventListener {
    @BindView(R.id.m_message_setting_all_text)
    TextView mMessageSettingAllText;
    @BindView(R.id.m_message_setting_comment_box)
    CheckBox mMessageSettingCommentBox;
    @BindView(R.id.m_message_setting_fans_box)
    CheckBox mMessageSettingFansBox;
    @BindView(R.id.m_message_setting_system_box)
    CheckBox mMessageSettingSystemBox;
    @BindView(R.id.m_message_setting_chat_box)
    CheckBox mMessageSettingChatBox;
    @BindView(R.id.m_message_setting_common_title)
    CommonTitleBar m_js_common_title;
    @Inject
    MessageSettingPresenter mPresenter;// 这个页面都没必要修改成MVP，

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_message_setting);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        NoticeSettingManager.requestPushSetting();
        updateView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("消息设置");

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NOTICE_SETTING, this);
    }

    private void updateView() {
        this.mMessageSettingCommentBox.setOnCheckedChangeListener(null);
        mMessageSettingFansBox.setOnCheckedChangeListener(null);
        mMessageSettingSystemBox.setOnCheckedChangeListener(null);
        mMessageSettingChatBox.setOnCheckedChangeListener(null);

        boolean isAllEnable = NoticeSettingManager.getAppNoticeState();
        mMessageSettingAllText.setText(isAllEnable ? "已开启" : "未开启");
        mMessageSettingCommentBox.setEnabled(isAllEnable);
        mMessageSettingFansBox.setEnabled(isAllEnable);
        mMessageSettingSystemBox.setEnabled(isAllEnable);
        mMessageSettingChatBox.setEnabled(isAllEnable);
        mMessageSettingCommentBox.setChecked(NoticeSettingManager.getNoticeCommentState() == 1);
        mMessageSettingFansBox.setChecked(NoticeSettingManager.getNoticeFansState() == 1);
        mMessageSettingSystemBox.setChecked(NoticeSettingManager.getNoticeSystemState() == 1);
        mMessageSettingChatBox.setChecked(NoticeSettingManager.getNoticeChatState() == 1);

        mMessageSettingCommentBox.setOnCheckedChangeListener((compoundButton, b) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE_MANAGE,
                    StatisticsConstant.OP_MESSAGE_MANAGE_COMMENT_SWITCH_CLICK);
            NoticeSettingManager.savePushSetting(mMessageSettingCommentBox.isChecked(), mMessageSettingFansBox.isChecked(),
                    mMessageSettingSystemBox.isChecked(), mMessageSettingChatBox.isChecked());
        });
        mMessageSettingFansBox.setOnCheckedChangeListener((compoundButton, b) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE_MANAGE,
                    StatisticsConstant.OP_MESSAGE_MANAGE_FANS_SWITCH_CLICK);
            NoticeSettingManager.savePushSetting(mMessageSettingCommentBox.isChecked(), mMessageSettingFansBox.isChecked(),
                    mMessageSettingSystemBox.isChecked(), mMessageSettingChatBox.isChecked());
        });
        mMessageSettingSystemBox.setOnCheckedChangeListener((compoundButton, b) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE_MANAGE,
                    StatisticsConstant.OP_MESSAGE_MANAGE_SYSTEM_SWITCH_CLICK);
            NoticeSettingManager.savePushSetting(mMessageSettingCommentBox.isChecked(), mMessageSettingFansBox.isChecked(),
                    mMessageSettingSystemBox.isChecked(), mMessageSettingChatBox.isChecked());
        });
        mMessageSettingChatBox.setOnCheckedChangeListener((compoundButton, b) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE_MANAGE,
                    StatisticsConstant.OP_MESSAGE_MANAGE_PRIVATE_CHAT_SWITCH_CLICK);
            NoticeSettingManager.savePushSetting(mMessageSettingCommentBox.isChecked(), mMessageSettingFansBox.isChecked(),
                    mMessageSettingSystemBox.isChecked(), mMessageSettingChatBox.isChecked());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NOTICE_SETTING, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NOTICE_SETTING) {// 需要刷新下
            updateView();
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
