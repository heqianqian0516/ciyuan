package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestCommentDetailApiParameter extends ApiParameter {
    private final String currentBizUuid;
    private final String userUuid;
    private final String bizUuid;

    public RequestCommentDetailApiParameter(String bizUuid, String commentUuid) {
        this.bizUuid = bizUuid;
        this.currentBizUuid = commentUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("currentBizUuid", new ApiParamMap.ParamData(this.currentBizUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("bizUuid", new ApiParamMap.ParamData(this.bizUuid));
        return map;
    }

}
