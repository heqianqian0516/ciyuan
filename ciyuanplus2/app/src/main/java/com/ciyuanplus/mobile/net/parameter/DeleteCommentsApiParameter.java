package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class DeleteCommentsApiParameter extends ApiParameter {
    private final String commentUuid;
    private final String userUuid;
    private final String postUuid;

    public DeleteCommentsApiParameter(String postUuid, String commentUuid) {
        this.postUuid = postUuid;
        this.commentUuid = commentUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
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
