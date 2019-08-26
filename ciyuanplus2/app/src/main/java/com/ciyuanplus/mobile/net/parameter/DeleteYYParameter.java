package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/23.
 */

public class DeleteYYParameter extends ApiParameter {

    private final String commentUuid;
    private final String userUuid;
    private final String postUuid;

    public DeleteYYParameter(String commentUuid, String userUuid, String postUuid) {
        this.commentUuid = commentUuid;
        this.userUuid = userUuid;
        this.postUuid = postUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("commentUuid", new ApiParamMap.ParamData(this.commentUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }
}
