package com.ciyuanplus.mobile.activity.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.MyProfileAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.account_manage.AccountManageActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.mine.change_head_icon.ChangeHeadIconActivity;
import com.ciyuanplus.mobile.module.mine.sign.ChangeSignActivity;
import com.ciyuanplus.mobile.module.settings.bind_phone.BindPhoneActivity;
import com.ciyuanplus.mobile.module.settings.change_name.ChangeNameActivity;
import com.ciyuanplus.mobile.module.settings.reset_password.ResetPasswordActivity;
import com.ciyuanplus.mobile.module.settings.select_sex.SelectSexPopActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.BindListBean;
import com.ciyuanplus.mobile.net.bean.MyProfileItem;
import com.ciyuanplus.mobile.net.parameter.ChangeBirthdayApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestOthersInfoApiParameter;
import com.ciyuanplus.mobile.net.response.BindListResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.datepicker.WheelMain;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

/**
 * Created by Alen on 2017/5/17.
 * 我的个人中心 编辑页面
 */

public class MyProfileActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, EventCenterManager.OnHandleEventListener, View.OnClickListener {
    @BindView(R.id.m_my_profile_list)
    ListView mMyProfileList;
    @BindView(R.id.m_my_profile_common_title)
    TitleBarView m_js_common_title;
    @BindView(R.id.tv_login_out)
    TextView mLoginOut;

    private MyProfileAdapter mAdapter;

    private ArrayList<MyProfileItem> mlist;
    private WheelMain wheelMain;

    int MESSAGE_CLEAR_FAIL = 1001;
    int MESSAGE_CLEAR_SUCCESS = 1000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_CLEAR_SUCCESS) {
                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_remove_cache_success_alert), Toast.LENGTH_SHORT).show();
                requestBindDetail();
            } else if (msg.what == MESSAGE_CLEAR_FAIL) {
                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_remove_cache_error_alert), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_my_profile);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        requestBindDetail();
//        updateView();
    }

    private void requestBindDetail() {

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_OTHER_PLATFORM_BIND_LIST);
        postRequest.setMethod(HttpMethods.Post);
        String uuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        postRequest.setHttpBody(new RequestOthersInfoApiParameter(uuid).getRequestBody());
        String authToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");

        postRequest.addHeader("authToken", authToken);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {

            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                BindListResponse res = new BindListResponse(s);
                if (!StringUtils.equals(res.mCode, ResponseData.CODE_OK)) {

                    CommonToast.getInstance(res.mMsg);
                } else {
                    updateView(res.mBindListBean);
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    private void initView() {
        ButterKnife.bind(this);
        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {

                onBackPressed();
            }
        });
        m_js_common_title.setTitle("设置");

        mLoginOut.setOnClickListener(this);

        mlist = new ArrayList<>();
        mAdapter = new MyProfileAdapter(this, mlist);
        this.mMyProfileList.setAdapter(mAdapter);
        this.mMyProfileList.setOnItemClickListener(this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this);// 如果用户信息有变化  需要更新下界面

    }

    //更新界面信息
    private void updateView(BindListBean bindListBean) {

        mlist.clear();
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_HEAD,
                getResources().getString(R.string.string_my_profile_head_alert),
                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().photo) ?
                        "" : UserInfoData.getInstance().getUserInfoItem().photo));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_NAME,
                getResources().getString(R.string.string_my_profile_name_alert),
                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().nickname) ?
                        "" : UserInfoData.getInstance().getUserInfoItem().nickname));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_SIGN,
                "签名",
                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().personalizedSignature) ?
                        "" : UserInfoData.getInstance().getUserInfoItem().personalizedSignature));

        mlist.add(new MyProfileItem(MyProfileItem.TYPE_SEX,
                getResources().getString(R.string.string_my_profile_sex_alert),
                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().getUserSex()) ?
                        "" : UserInfoData.getInstance().getUserInfoItem().getUserSex()));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_BIRTHDAY,
                getResources().getString(R.string.string_my_profile_birthday_alert), UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().getUserSex()) ?
                "" : UserInfoData.getInstance().getUserInfoItem().birthday));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_PHONE,
                getResources().getString(R.string.string_my_profile_phone_alert),
                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().mobile) ? "绑定手机" : UserInfoData.getInstance().getUserInfoItem().mobile));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_CACHE,
                getResources().getString(R.string.string_my_profile_cache_alert), CacheManager.getInstance().getCacheSize()));

//        MyProfileItem accountItem = new MyProfileItem(MyProfileItem.TYPE_ACCOUNT_MANAGE, "账号管理", "");
        MyProfileItem accountItem = new MyProfileItem(MyProfileItem.TYPE_ACCOUNT_MANAGE, "账号管理", "", bindListBean.isWxBind(), bindListBean.isSinaBind(), bindListBean.isQQBind());


        mlist.add(accountItem);

//        if (UserInfoData.getInstance().getUserInfoItem() != null && UserInfoData.getInstance().getUserInfoItem().isPassword == 1)
//            mlist.add(new MyProfileItem(MyProfileItem.TYPE_PASSWORD,
//                    getResources().getString(R.string.string_my_profile_password_alert), ""));// 如果没有设置过密码   不显示这个选项
        mAdapter.notifyDataSetChanged();
//        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id == -1) {
            return;
        }
        int index = (int) id;
        if (Utils.isStringEquals(MyProfileItem.TYPE_PHONE, mAdapter.getItem(index).type)) {// 绑定手机号
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CHANGE_PHONE_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, BindPhoneActivity.class);
            this.startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_PASSWORD, mAdapter.getItem(index).type)) {// 修改密码
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CHANGE_PASSWORD_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            this.startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_HEAD, mAdapter.getItem(index).type)) {// 选择头像
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CHANGE_HEAD_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, ChangeHeadIconActivity.class);// 头像
            startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_NAME, mAdapter.getItem(index).type)) {// 填写姓名
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CHANGE_NAME_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, ChangeNameActivity.class);
            this.startActivity(intent);
            // 选择性别
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_SEX, mAdapter.getItem(index).type)) {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CHANGE_SEX_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, SelectSexPopActivity.class);
            this.startActivity(intent);
            // 选择生日
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_BIRTHDAY, mAdapter.getItem(index).type)) {
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            LayoutInflater inflater = LayoutInflater.from(this);
            final View timepickerview = inflater.inflate(R.layout.timepicker,
                    null);
            Calendar calendar = Calendar.getInstance();

            wheelMain = new WheelMain(timepickerview);
            wheelMain.screenheight = Utils.getScreenHeight();
            String time = UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().getUserSex()) ?
                    calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) : UserInfoData.getInstance().getUserInfoItem().birthday;
            if (Utils.isDate(time, "yyyy-MM-dd")) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    calendar.setTime(dateFormat.parse(time));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            wheelMain.initDateTimePicker(year, month, day);
            new CustomDialog.Builder(this)
                    .setTitle("选择生日")
                    .setContentView(timepickerview)
                    .setPositiveButton("确定",
                            (dialog, which) -> {
                                changeBirthday(wheelMain.getTime());
                                dialog.dismiss();
                            })
                    .setNegativeButton("取消",
                            (dialog, which) -> dialog.dismiss()).create().show();
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_CACHE, mAdapter.getItem(position).type)) {// 清理缓存
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_CLEAR_CACHE_CLICK);
            new Thread(() -> {
                boolean clearResult = CacheManager.getInstance().clearCacheFiles();
                if (clearResult) {
                    mHandler.sendEmptyMessage(MESSAGE_CLEAR_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(MESSAGE_CLEAR_FAIL);
                }
            }).start();
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_ACCOUNT_MANAGE, mAdapter.getItem(position).type)) {

            Intent intent = new Intent(mActivity, AccountManageActivity.class);
            startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_SIGN, mAdapter.getItem(position).type)) {
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, ChangeSignActivity.class);
            intent.putExtra(Constants.INTENT_SIGN, mAdapter.getItem(position).value);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE) {
            requestBindDetail();
        }
    }

    // 修改生日
    private void changeBirthday(final String date) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_BRITHDAY);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangeBirthdayApiParameter(date).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance("修改成功", Toast.LENGTH_SHORT).show();

                    UserInfoData.getInstance().getUserInfoItem().birthday = date;
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE));
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_head_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login_out:
                if (LoginStateManager.isLogin()) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(this);
                    builder.setMessage("是否退出账号？");
                    builder.setPositiveButton("退出", (dialog, which) -> {

                        dialog.dismiss();
                        LoginStateManager.logout();

                    });
                    builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                    CustomDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }

                break;
        }
    }
}

