package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class ForgetPasswordApiParameter extends ApiParameter {
    private final String mobile;
    private final String code;
    private final String password;
    private final String type;

    public ForgetPasswordApiParameter(String mobile, String code, String password, String type) {
        this.mobile = mobile;
        this.code = code;
        this.password = password;
        this.type = type;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("mobile", new ApiParamMap.ParamData(this.mobile));
        map.put("code", new ApiParamMap.ParamData(this.code));
        map.put("type", new ApiParamMap.ParamData(this.type));
        map.put("password", new ApiParamMap.ParamData(this.password));
        return map;
    }
}
