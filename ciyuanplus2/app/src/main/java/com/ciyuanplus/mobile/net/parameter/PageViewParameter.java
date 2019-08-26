package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/4/26.
 */

public class PageViewParameter extends ApiParameter {

    private final String postUuid;

    public PageViewParameter(String postUuid) {
        this.postUuid = postUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }
}
