package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangeNameApiParameter extends ApiParameter {
    private final String nickname;
    private final String userUuid;

    public ChangeNameApiParameter(String nickname) {
        this.nickname = nickname;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("nickname", new ApiParamMap.ParamData(this.nickname));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
