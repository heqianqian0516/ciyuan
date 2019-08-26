package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestLoginUserInfoResponse extends ResponseData {
    public int fansCount;
    public int followCount;
    public int isFans;
    public int likeCount;

    public RequestLoginUserInfoResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            JSONObject mObject1 = new JSONObject(data1);//
            fansCount = mObject1.getInt("fansCount");
            followCount = mObject1.getInt("followCount");
            likeCount = mObject1.getInt("likeCount");
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
    }
}
