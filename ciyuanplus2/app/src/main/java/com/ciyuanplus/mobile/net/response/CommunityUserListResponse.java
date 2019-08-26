package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommunityUserListInfo;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by Alen on 2017/2/11.
 */
public class CommunityUserListResponse extends ResponseData {
    public CommunityUserListInfo communityUserListInfo;

    public CommunityUserListResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();
        communityUserListInfo = gson.fromJson(data, CommunityUserListInfo.class);
//
//        try {
//            JSONObject mObject= new JSONObject(data);//
//            String data1 =mObject.getString("data");
//            noticeListInfo = gson.fromJson(data1, NoticeListInfoItem.class);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
