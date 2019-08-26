package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.DeleteCommentOrReplyItem;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kk on 2018/5/16.
 */

public class DeleteCommentOrReplyResponse extends ResponseData {

    public DeleteCommentOrReplyItem deleteCommentOrReplyItem;

    public DeleteCommentOrReplyResponse(String data) {
        super(data);

        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();

        try {
            JSONObject mObject = new JSONObject(data);//
            String data1 = mObject.getString("data");
            deleteCommentOrReplyItem = gson.fromJson(data1, DeleteCommentOrReplyItem.class);

        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }

    }
}
