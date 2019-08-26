package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/16.
 */

public class MyActionParameter extends ApiParameter {

    private final String communityUuid;
    private final String userUuid;

    public MyActionParameter(String communityUuid, String userUuid) {
        this.communityUuid = communityUuid;
        this.userUuid = userUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;

    }
}
