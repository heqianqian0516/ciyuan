package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/10.
 */

public class Dengluparameter extends ApiParameter {

    private final String loginName;
    private final String password;

    public Dengluparameter(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    @Override
    public ApiParamMap buildExtraParameter() {

        ApiParamMap map = new ApiParamMap();
        map.put("loginName", new ApiParamMap.ParamData(this.loginName));
        map.put("password", new ApiParamMap.ParamData(this.password));
        return map;

    }
}
