package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class AutoLoginApiParameter extends ApiParameter {
    private final String type;
    private final String userUUID;

    public AutoLoginApiParameter(String userUUID, String type) {
        this.userUUID = userUUID;
        this.type = type;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUUID));
        map.put("type", new ApiParamMap.ParamData(this.type));
        return map;
    }
}
