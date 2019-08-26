package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class BindPhoneApiParameter extends ApiParameter {
    private final String mobile;
    private final String code;
    private final String type;
    private final String userUuid;

    public BindPhoneApiParameter(String mobile, String code, String type) {
        this.mobile = mobile;
        this.code = code;
        this.type = type;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("code", new ApiParamMap.ParamData(this.code));
        map.put("type", new ApiParamMap.ParamData(this.type));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
