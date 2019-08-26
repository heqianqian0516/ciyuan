package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangeStuffStatusApiParameter extends ApiParameter {
    private final String postUuid;
    private final String status;
    private final String userUuid;

    public ChangeStuffStatusApiParameter(String postUuid, String status) {
        this.postUuid = postUuid;
        this.status = status;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("status", new ApiParamMap.ParamData(this.status));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
