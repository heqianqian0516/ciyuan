package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class LoginApiParameter extends ApiParameter {
    private final String type;
    private final String mobile;
    private final String smsCode;

    public LoginApiParameter(String mobile, String smsCode, String type) {
        this.mobile = mobile;
        this.smsCode = smsCode;
        this.type = type;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("smsCode", new ApiParamMap.ParamData(this.smsCode));
        map.put("type", new ApiParamMap.ParamData(this.type));
        return map;
    }
}
