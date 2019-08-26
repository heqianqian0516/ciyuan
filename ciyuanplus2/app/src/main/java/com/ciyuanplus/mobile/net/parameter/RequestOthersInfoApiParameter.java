package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestOthersInfoApiParameter extends ApiParameter {
    private final String userUuid;
    private final String loginUserUuid;
    private final String userState;

    public RequestOthersInfoApiParameter(String userid) { //请求其他人
        this.userUuid = userid;
        this.loginUserUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("loginUserUuid", new ApiParamMap.ParamData(this.loginUserUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        return map;
    }
}
