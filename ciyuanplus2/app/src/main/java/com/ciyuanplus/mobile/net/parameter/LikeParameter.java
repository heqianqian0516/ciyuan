package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/20.
 */

public class LikeParameter extends ApiParameter {

    private final String postUuid;
    private final String commentUuid;
    private final String userUuid;
    private final String bizType;

    public LikeParameter(String postUuid, String commentUuid, String userUuid, String bizType) {
        this.postUuid = postUuid;
        this.commentUuid = commentUuid;
        this.userUuid = userUuid;
        this.bizType = bizType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {

        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        return map;
    }
}
