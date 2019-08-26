package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.HelpListItem;
import com.ciyuanplus.mobile.net.bean.HomeADBean;
import com.ciyuanplus.mobile.net.bean.RankedFirstBean;
import com.ciyuanplus.mobile.net.bean.RankedListBean;
import com.ciyuanplus.mobile.net.bean.RankingListBean;
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
public class RequestRankedFirstResponse extends ResponseData {

    public RankedFirstBean rankedFirstBean;

    public RequestRankedFirstResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        Gson gson = GsonUtils.getGsson();
        rankedFirstBean = gson.fromJson(data, RankedFirstBean.class);

    }
}
