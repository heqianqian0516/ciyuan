package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetSystemMessageDetailApiParameter extends ApiParameter {

    private final String userUuid;
    private final String uuid;

    public GetSystemMessageDetailApiParameter(String uuid) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.uuid = uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("uuid", new ApiParamMap.ParamData(this.uuid));
        return map;
    }

}
