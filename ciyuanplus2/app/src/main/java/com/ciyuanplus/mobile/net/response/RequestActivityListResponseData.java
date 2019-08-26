package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.ActivityItem;
import com.ciyuanplus.mobile.net.bean.FreshNewListItem;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestActivityListResponseData extends ResponseData {
    public ActivityItem activityItem;
    public RequestActivityListResponseData(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            activityItem = gson.fromJson(data1, ActivityItem.class);
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
    }
}
