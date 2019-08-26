package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangeBirthdayApiParameter extends ApiParameter {
    private final String birth;
    private final String userUuid;

    public ChangeBirthdayApiParameter(String birthday) {
        this.birth = birthday;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("birth", new ApiParamMap.ParamData(this.birth));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
