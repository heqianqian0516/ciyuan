package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.ParticipationItembean;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestParticipationDetailResponse extends ResponseData {


    public ParticipationItembean participationItembean;

    public RequestParticipationDetailResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;

        Gson gson = GsonUtils.getGsson();
        participationItembean = gson.fromJson(data, ParticipationItembean.class);

    }
}
