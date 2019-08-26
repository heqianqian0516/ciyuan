package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class LoginResponse extends ResponseData {
    public UserInfoItem userInfoItem;
    public String rawData;
    public String token;

    public LoginResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;
        //到这里已经解析出来 code  和 msg 了
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            JSONObject mObject1 = new JSONObject(data1);//
            token = mObject1.getString("token");
            rawData = mObject1.getString("user");
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
        Gson gson = GsonUtils.getGsson();
        userInfoItem = gson.fromJson(rawData, UserInfoItem.class);
    }
}
