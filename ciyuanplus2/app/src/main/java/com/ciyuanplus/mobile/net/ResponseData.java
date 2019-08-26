package com.ciyuanplus.mobile.net;

import android.content.Intent;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class ResponseData {
    public static final String CODE_OK = "1";
    public static String CODE_FAIL = "0";
    private static final String CODE_FROZEN = "3";
    public static final String CODE_NULL_DATA = "-1";
    public static final String CODE_NO_USER = "4";
    private static final String CODE_NOT_LOGIN = "2";
    public String mCode;
    public String mMsg;
    private JSONObject mObject;
    public String mRawDataString;

    public ResponseData(String data) {

        try {

            mObject = new JSONObject(data);//应该是response
            mCode = mObject.getString("code");
            mMsg = mObject.getString("msg");
            mRawDataString = mObject.getString("data");
            if (Utils.isStringEmpty(data)) {
                Logger.d("ResponseData: " + data);
            }
            if (Utils.isStringEquals(mCode, CODE_FROZEN) || Utils.isStringEquals(mCode, CODE_NOT_LOGIN)) {
                if (!Utils.isStringEmpty(mMsg)) {
                    CommonToast.getInstance(mMsg).show();
                }
                if (LoginStateManager.isLogin()) {
                    LoginStateManager.forceLogout();
                }
                Intent intent = new Intent(App.mContext, LoginActivity.class);//去登陆页面
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                App.mContext.startActivity(intent);
            }
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }

    }
}
