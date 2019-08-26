package com.ciyuanplus.mobile.fragement;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.MessageCenterActivity;
import com.ciyuanplus.mobile.activity.chat.InviteActivity;
import com.ciyuanplus.mobile.activity.chat.SystemMessageListActivity;
import com.ciyuanplus.mobile.activity.chat.UserMessageListActivity;
import com.ciyuanplus.mobile.adapter.MyMessageAdapter;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.MyMessagesItem;
import com.ciyuanplus.mobile.net.parameter.GetNoticeCountApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetNoticeListApiParameter;
import com.ciyuanplus.mobile.net.response.NoticeCountResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.ListViewForScrollView;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/5/11.
 */

public class ChatFragment extends MyFragment implements EventCenterManager.OnHandleEventListener {

    @BindView(R.id.m_main_chat_invate_image)
    LinearLayout mMainChatInvateImage;
    @BindView(R.id.m_main_chat_top_lp)
    RelativeLayout mMainChatTopLp;
    @BindView(R.id.m_main_chat_list)
    ListViewForScrollView mMainChatList;
    @BindView(R.id.title_bar)
    TitleBarView mTitleBarView;

    private MyMessageAdapter mAdapter;
    private final ArrayList<MyMessagesItem> mList = new ArrayList<>();
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mMainChatInvateImage.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {

                getActivity();
                if (((MessageCenterActivity) getActivity()).mDotInfoResponse.isMenuEvent == 1) {
                    if (!LoginStateManager.isLogin()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        return;
                    }
                    Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
                    intent.putExtra(Constants.INTENT_OPEN_URL, ((MessageCenterActivity) getActivity()).mDotInfoResponse.urlMenu);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

    }


    private void initData() {

        initTitleBar();
        mList.clear();
//        mList.add(new MyMessagesItem(5, 0));
        mList.add(new MyMessagesItem(1, 0));
        mList.add(new MyMessagesItem(2, 0));
        mList.add(new MyMessagesItem(3, 0));
//        mList.add(new MyMessagesItem(4, 0));
//        getRongConversationList();
        mAdapter = new MyMessageAdapter(getActivity(), mList);
        mMainChatList.setAdapter(mAdapter);
        mMainChatList.setOnItemClickListener((adapterView, view, i, l) -> {
            if (l == -1) {
                return;
            }
            int postion = (int) l;
            Intent intent;
            switch (postion) {
                case 0:
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE, StatisticsConstant.OP_MESSAGE_COMMENTS_CLICK);

                    if (!LoginStateManager.isLogin()) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    intent = new Intent(getActivity(), UserMessageListActivity.class);
                    intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, GetNoticeListApiParameter.TYPE_NEWS);
                    getActivity().startActivity(intent);
                    break;
                case 1:
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE, StatisticsConstant.OP_MESSAGE_FANS_CLICK);

                    if (!LoginStateManager.isLogin()) {
                        SharedPreferencesManager.putBoolean("Mainlayout", "main", false);
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    intent = new Intent(getActivity(), UserMessageListActivity.class);
                    intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, GetNoticeListApiParameter.TYPE_FOLLOW);
                    getActivity().startActivity(intent);
                    break;
                case 2:
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MESSAGE, StatisticsConstant.OP_MESSAGE_SYSTEM_CLICK);

                    intent = new Intent(getActivity(), SystemMessageListActivity.class);
                    getActivity().startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    private void initTitleBar() {

        mTitleBarView.setTitle("消息");
        mTitleBarView.setOnBackListener(v -> getActivity().finish());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestNoticeCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT, this);// 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_CHAT_CONVERSATION_LIST_ITEM, this);
        requestNoticeCount();

        //清除系统通知
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    // 获取消息提醒个数
    private void requestNoticeCount() {
        {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_NOTICE_COUNT_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new GetNoticeCountApiParameter().getRequestBody());
            String sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
            if (!Utils.isStringEmpty(sessionKey)) {
                postRequest.addHeader("authToken", sessionKey);
            }
            postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    super.onSuccess(s, response);
                    NoticeCountResponse response1 = new NoticeCountResponse(s);
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        mList.get(0).setNumber(response1.countItem.postCount);
                        mList.get(1).setNumber(response1.countItem.followCount);
                        mList.get(2).setNumber(response1.countItem.systemMessageCount);
//                        mList.get(4).setNumber(response1.countItem.feedbackMessageCount);
                        mAdapter.notifyDataSetChanged();
//                        if (getActivity() != null)
//                            ((MessageCenterActivity) getActivity()).updateUnReadMessage(response1.countItem.postCount + response1.countItem.followCount
//                                    + response1.countItem.systemMessageCount + response1.countItem.feedbackMessageCount);
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        // 添加小区列表变化的监听事件
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_CHAT_CONVERSATION_LIST_ITEM, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT) {
            requestNoticeCount();
        }
    }
}
