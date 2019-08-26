package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class UserAddCommunityApiParameter extends ApiParameter {
    private final String userUuid;
    private final String communityUuid;

    public UserAddCommunityApiParameter(String userUuid, String communityUuid) {
        this.userUuid = userUuid;
        this.communityUuid = communityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }
}
