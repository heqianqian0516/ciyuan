package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.ActivityItemBean;
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
public class RequestActivityResponse extends ResponseData {
    public ActivityItemBean activityItemBean;
    public String rawData ;

    public RequestActivityResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            Gson gson = GsonUtils.getGsson();
            rawData = data1;
            activityItemBean= gson.fromJson(data1, ActivityItemBean.class);

        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }

    }
}
