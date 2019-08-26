package com.ciyuanplus.mobile.manager;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.RequestPushSettingApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestSavePushSettingApiParameter;
import com.ciyuanplus.mobile.net.response.RequestPushSettingResponse;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Alen on 2017/6/22.
 * 推送消息页面
 */

public class NoticeSettingManager {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    private static boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } catch (InvocationTargetException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } catch (IllegalAccessException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
        return false;
    }

    public static boolean getAppNoticeState() {
        boolean isOpened = isNotificationEnabled(App.mContext);
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(App.mContext);
//        boolean isOpened = manager.areNotificationsEnabled();
        return isOpened;
    }

    public static int getNoticeCommentState() {
        return SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_COMMENT_INFO, 1);
    }

    private static void setNoticeCommentState(int value) {
        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_COMMENT_INFO, value);
    }

    public static int getNoticeFansState() {
        return SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_FANS_INFO, 1);
    }

    private static void setNoticeFansState(int value) {
        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_FANS_INFO, value);
    }

    public static int getNoticeSystemState() {
        return SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_SYSTEM_INFO, 1);
    }

    private static void setNoticeSystemState(int value) {
        if (value == 1) { // 设置接受系统消息
            setUpushSystemGroup();
        } else {// 设置静默接受系统消息
            setUpushScilentGroup();
        }
        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_SYSTEM_INFO, value);
    }

    public static int getNoticeChatState() {
        return SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_CHAT_INFO, 1);
    }

    private static void setNoticeChatState(int value) {
        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_NOTICE_SETTING_CHAT_INFO, value);
//        if (value == 0)
//            RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, null);// 设置全部静默
//        else RongIM.getInstance().removeNotificationQuietHours(null);// 设置非静默
    }

    public static void requestPushSetting() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_PUSH_SETTING_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPushSettingApiParameter().getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestPushSettingResponse response1 = new RequestPushSettingResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    NoticeSettingManager.setNoticeCommentState(response1.pushSettingItem.commentPush);
                         NoticeSettingManager.setNoticeFansState(response1.pushSettingItem.followerPush);
                    NoticeSettingManager.setNoticeSystemState(response1.pushSettingItem.systemmessagePush);
                    NoticeSettingManager.setNoticeChatState(response1.pushSettingItem.chatmessagePush);

                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NOTICE_SETTING));
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public static void savePushSetting(boolean commentChecked, boolean fansChecked, boolean systemChecked, boolean chatChecked) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SAVE_PUSH_SETTING_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestSavePushSettingApiParameter(commentChecked,
                fansChecked, systemChecked, chatChecked).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    requestPushSetting();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_password_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /***
     *  系统消息 注册 分组 逻辑：
     *  临时用户 注册为系统消息接受分组
     *  注册登录用户   默认为 系统消息接受分组
     *  正式用户登录   根据设置修改系统消息  接受或者静音分组
     *  用户退出   取消所有的分组
     *  用户在其他地方登录被登出  取消所有分组
     * */
    public static void setUpushSystemGroup() {
        App.mPushAgent.deleteAlias("gag", "SYSTEM_MESSAGE_NODELELENG", (isSuccess, message) -> {
        });
        App.mPushAgent.addAlias("gag", "SYSTEM_MESSAGE", (isSuccess, message) -> {
        });
    }

    private static void setUpushScilentGroup() {
        App.mPushAgent.deleteAlias("gag", "SYSTEM_MESSAGE", (isSuccess, message) -> {
        });
        App.mPushAgent.addAlias("gag", "SYSTEM_MESSAGE_NODELELENG", (isSuccess, message) -> {
        });
    }

    public static void removeUpushGroup() {
        App.mPushAgent.deleteAlias("gag", "SYSTEM_MESSAGE", (isSuccess, message) -> {
        });
        App.mPushAgent.deleteAlias("gag", "SYSTEM_MESSAGE_NODELELENG", (isSuccess, message) -> {
        });
    }
}
