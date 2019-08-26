package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestBannerApiParameter extends ApiParameter {
    private final String showSection;
    private final String userState;

    public RequestBannerApiParameter(String showSection) {
        this.showSection = showSection;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("showSection", new ApiParamMap.ParamData(this.showSection));
        map.put("userState", new ApiParamMap.ParamData(this.userState));

        return map;
    }
}
