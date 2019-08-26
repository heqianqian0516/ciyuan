package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.orhanobut.logger.Logger;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestPostDetailApiParameter extends ApiParameter {
    private final String userUuid;
    private final String userState;
    private final String postUuid;

    public RequestPostDetailApiParameter(String postUuid) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.postUuid = postUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        Logger.d("用户id = " + this.userUuid + ", 他人id = " + this.postUuid);
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }

}
