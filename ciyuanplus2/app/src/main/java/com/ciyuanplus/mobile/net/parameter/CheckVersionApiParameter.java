package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2017/2/11.
 */
public class CheckVersionApiParameter extends ApiParameter {
    private final String channelCode;
    private final String appType;

    public CheckVersionApiParameter() {
        this.channelCode = Utils.getChannelName();
        this.appType = "20";
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("channelCode", new ApiParamMap.ParamData(this.channelCode));
        map.put("appType", new ApiParamMap.ParamData(this.appType));
        return map;
    }

}
