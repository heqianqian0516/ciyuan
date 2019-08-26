package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/21.
 */

public class CancellikeParameter extends ApiParameter {

    private final String commentUuid;
    private final String userUuid;

    public CancellikeParameter(String commentUuid, String userUuid) {
        this.commentUuid = commentUuid;
        this.userUuid = userUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {

        ApiParamMap map = new ApiParamMap();
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
