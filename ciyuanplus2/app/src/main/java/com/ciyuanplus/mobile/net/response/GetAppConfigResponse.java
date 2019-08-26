package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.AppConfigItem;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetAppConfigResponse extends ResponseData {
    @Nullable
    public AppConfigItem appConfigItem;
    @Nullable
    public String postTypeString;
    @Nullable
    public String wikiTypeDictString;

    public GetAppConfigResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            appConfigItem = gson.fromJson(data1, AppConfigItem.class);
            JSONObject mObject1 = new JSONObject(data1);
            postTypeString = mObject1.getString("postTypeDict");
            wikiTypeDictString = mObject1.getString("wikiTypeDict");
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }

    }
}