package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.BannerListItem;
import com.ciyuanplus.mobile.net.bean.HomeADBean;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestAdvertisementListResponse extends ResponseData {
    private static String result;
    public BannerListItem bannerListItem;

    public RequestAdvertisementListResponse(String data) {
        super(data);
    }

    public static ArrayList<HomeADBean.DataBean> RequestAdvertisementList(String data) {
        Gson gson = GsonUtils.getGsson();
        List<HomeADBean.DataBean> data1 = null;
        try {
            JSONObject mObject = new JSONObject(data);//
            result = data;
            HomeADBean homeADBean = gson.fromJson(result, HomeADBean.class);
             data1 = homeADBean.getData();
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }
        return (ArrayList<HomeADBean.DataBean>) data1;

    }

}
