package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class DeleteCommunityApiParameter extends ApiParameter {
    private final String communityUuid;
    private final String userUuid;

    public DeleteCommunityApiParameter(String communityId) {
        this.communityUuid = communityId;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
