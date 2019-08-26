package com.ciyuanplus.mobile.module.settings.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.MyProfileAdapter;
import com.ciyuanplus.mobile.manager.AppVersionManager;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.settings.about.AboutActivity;
import com.ciyuanplus.mobile.module.settings.address.AddressManagerActivity;
import com.ciyuanplus.mobile.module.settings.help.HelpActivity;
import com.ciyuanplus.mobile.module.settings.message_setting.MessageSettingActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.MyProfileItem;
import com.ciyuanplus.mobile.net.parameter.CheckVersionApiParameter;
import com.ciyuanplus.mobile.net.response.CheckVersionResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class SettingsPresenter implements SettingsContract.Presenter, AdapterView.OnItemClickListener {
    private static final int MESSAGE_CLEAR_SUCCESS = 1000;
    private static final int MESSAGE_CLEAR_FAIL = 1001;
    private CheckVersionResponse mAppVersionResponse;
    private final SettingsContract.View mView;
    private MyProfileAdapter mAdapter;
    private File tempFile;//选择头像相关的文件
    private String mHeadIconPath;
    private ArrayList<MyProfileItem> mlist;
    private MyProfileItem mAppVersionItem;
    private final Handler mHandler;

    @Inject
    public SettingsPresenter(SettingsContract.View mView) {
        this.mView = mView;
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE_CLEAR_SUCCESS) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_remove_cache_success_alert), Toast.LENGTH_SHORT).show();
                    initList();
                } else if (msg.what == MESSAGE_CLEAR_FAIL) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_remove_cache_error_alert), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void checkAppVersion() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_APP_VERSION_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CheckVersionApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                CheckVersionResponse response1 = new CheckVersionResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.appVersion != null) {
                    mAppVersionResponse = response1;
                    if (response1.appVersion.versionCode > Utils.getVersionCode()) { //有更新
                        mAppVersionItem.value = "存在更新版本";
                    } else {
                        mAppVersionItem.value = "已是最新版本";
                        //CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_app_version_newest_alert)).show();
                    }
                    mAdapter.notifyDataSetChanged();
                }  //                    CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_app_version_error_alert)).show();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void initData(ListView mMySettingList) {
        mlist = new ArrayList<>();
        mAdapter = new MyProfileAdapter(mView.getDefaultContext(), mlist);
        mMySettingList.setAdapter(mAdapter);
        mMySettingList.setOnItemClickListener(this);

        initList();


    }

    private void initList() {
        mlist.clear();
        mAppVersionItem = new MyProfileItem(MyProfileItem.TYPE_UPDATE, "版本更新", "");// 需要异步更新
        //checkAppVersion();
//        mlist.add(new MyProfileItem(MyProfileItem.TYPE_IDENTIFY,
//                mContext.getResources().getString(R.string.string_my_profile_identify_alert),
//                UserInfoData.getInstance().getUserInfoItem() == null || Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().getUserState())?
//                        "未认证" :UserInfoData.getInstance().getUserInfoItem().getUserState()));

        mlist.add(new MyProfileItem(MyProfileItem.TYPE_MESSAGE,
                mView.getDefaultContext().getResources().getString(R.string.string_my_profile_message_alert), ""));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_CACHE,
                mView.getDefaultContext().getResources().getString(R.string.string_my_profile_cache_alert), CacheManager.getInstance().getCacheSize()));
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_HELP,
                mView.getDefaultContext().getResources().getString(R.string.string_my_profile_help_alert), ""));
        if (Utils.isDebug()) mlist.add(new MyProfileItem(MyProfileItem.TYPE_CHANGE_SERVER,
                SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SERVER_SET,
                        Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 0) == 0 ? "测试服务器" : "正式服务器", ""));
        //mlist.add(mAppVersionItem);
        mlist.add(new MyProfileItem(MyProfileItem.TYPE_ABOUT,
                mView.getDefaultContext().getResources().getString(R.string.string_my_profile_about_alert), ""));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id == -1) {
            return;
        }
        int index = (int) id;
        if (Utils.isStringEquals(MyProfileItem.TYPE_IDENTIFY, mAdapter.getItem(index).type)) {// 认证

//            if (!LoginStateManager.isLogin()) {
//                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
//                mView.getDefaultContext().startActivity(intent);
//                return;
//            }
//            Intent intent = new Intent(mView.getDefaultContext(), IndentifyActivity.class);
//            mView.getDefaultContext().startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_ADDRESS, mAdapter.getItem(index).type)) {// 地址管理
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_ADDRESS_MANAGE_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
                mView.getDefaultContext().startActivity(intent);
                return;
            }
            Intent intent = new Intent(mView.getDefaultContext(), AddressManagerActivity.class);
            mView.getDefaultContext().startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_CACHE, mAdapter.getItem(index).type)) {// 清理缓存
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
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_HELP, mAdapter.getItem(index).type)) {// 帮助
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_HELP_CLICK);
            Intent intent = new Intent(mView.getDefaultContext(), HelpActivity.class);
            mView.getDefaultContext().startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_ABOUT, mAdapter.getItem(index).type)) {// 关于我们
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_ABOUT_US_CLICK);
            Intent intent = new Intent(mView.getDefaultContext(), AboutActivity.class);
            mView.getDefaultContext().startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_MESSAGE, mAdapter.getItem(index).type)) {// 消息设置
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SETTING,
                    StatisticsConstant.OP_SETTING_MESSAGE_MANAGE_CLICK);
            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
                mView.getDefaultContext().startActivity(intent);
                return;
            }
            Intent intent = new Intent(mView.getDefaultContext(), MessageSettingActivity.class);
            mView.getDefaultContext().startActivity(intent);
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_UPDATE, mAdapter.getItem(index).type)) {// 消息设置
            if (mAppVersionResponse == null) return;
            if (mAppVersionResponse.appVersion.versionCode > Utils.getVersionCode()) { //有更新
                AppVersionManager.showUpdateDialog(mAppVersionResponse.appVersion.versionCode, mAppVersionResponse.appVersion.url, (Activity) mView.getDefaultContext());

            } else {
                CommonToast.getInstance("已是最新版本!").show();
                //CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_app_version_newest_alert)).show();
            }
        } else if (Utils.isStringEquals(MyProfileItem.TYPE_CHANGE_SERVER, mAdapter.getItem(index).type)) {// 消息设置
            int type = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SERVER_SET, Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 0);
            if (type == 0) {// 测试环境
                SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SERVER_SET, Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 1);
            } else {
                SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SERVER_SET, Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 0);
            }
            SharedPreferencesManager.clearAllSets(Constants.SHARED_PREFERENCES_SET);
            CommonToast.getInstance("已经设置完成，请后台直接杀死该App,然后重启").show();
            // 设置完成之后
        }
    }

    @Override
    public void detachView() {
    }
}
