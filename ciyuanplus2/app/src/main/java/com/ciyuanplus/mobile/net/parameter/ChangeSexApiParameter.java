package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ChangeSexApiParameter extends ApiParameter {
    private final String sex;
    private final String userUuid;

    public ChangeSexApiParameter(int sex) {
        this.sex = sex + "";
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("sex", new ApiParamMap.ParamData(this.sex));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
