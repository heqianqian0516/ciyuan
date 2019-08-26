package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.BindListBean;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class BindListResponse extends ResponseData {
    public BindListBean mBindListBean;

    public BindListResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) {
            return;
        }

        Gson gson = GsonUtils.getGsson();
        try {
            JSONObject dataOrig = new JSONObject(data);//
            String dataBean = dataOrig.getString("data");
            mBindListBean = gson.fromJson(dataBean, BindListBean.class);
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }
    }
}
