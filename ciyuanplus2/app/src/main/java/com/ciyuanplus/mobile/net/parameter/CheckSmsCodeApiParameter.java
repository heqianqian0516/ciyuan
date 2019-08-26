package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class CheckSmsCodeApiParameter extends ApiParameter {
    private final String mobile;
    private final String code;
    private final String type;

    public CheckSmsCodeApiParameter(String mobile, String code, String type) {
        this.mobile = mobile;
        this.type = type;
        this.code = code;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("code", new ApiParamMap.ParamData(this.code));
        map.put("type", new ApiParamMap.ParamData(this.type));
        return map;
    }
}
