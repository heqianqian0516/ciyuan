package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.RankingListBean;
import com.ciyuanplus.mobile.net.bean.TaskNumberListBean;
import com.ciyuanplus.mobile.net.bean.UserSignDataBean;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class UserSignDateResponse extends ResponseData {
    public UserSignDataBean userSignDataBean;


    public UserSignDateResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        Gson gson = GsonUtils.getGsson();

        userSignDataBean = gson.fromJson(data, UserSignDataBean.class);

    }
}
