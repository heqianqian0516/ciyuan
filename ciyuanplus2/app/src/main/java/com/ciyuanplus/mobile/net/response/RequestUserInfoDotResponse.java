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
public class RequestUserInfoDotResponse extends ResponseData {
    private int isUnread;
    public int isMenuEvent;
    public String urlMenu;
    private String menuImage;
    private String menuText;

    public RequestUserInfoDotResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            JSONObject mObject1 = new JSONObject(data1);//
            isUnread = mObject1.getInt("isUnread");
            isMenuEvent = mObject1.getInt("isMenuEvent");
            urlMenu = mObject1.getString("urlMenu");
            menuImage = mObject1.getString("menuImage");
            menuText = mObject1.getString("menuText");

        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
    }
}
