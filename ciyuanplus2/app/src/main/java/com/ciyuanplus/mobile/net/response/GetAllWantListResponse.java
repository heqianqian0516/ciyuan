package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.AllWantListItem;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetAllWantListResponse extends ResponseData {
    public AllWantListItem allWantListItem;

    public GetAllWantListResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        //到这里已经解析出来 code  和 msg 了
        Gson gson = GsonUtils.getGsson();
        allWantListItem = gson.fromJson(data, AllWantListItem.class);

    }
}
