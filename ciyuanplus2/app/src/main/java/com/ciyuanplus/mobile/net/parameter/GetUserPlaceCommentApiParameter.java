package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetUserPlaceCommentApiParameter extends ApiParameter {
    private final String placeId;
    private final String userUuid;
    private final String userState;

    public GetUserPlaceCommentApiParameter(String placeId) {
        this.placeId = placeId;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("placeId", new ApiParamMap.ParamData(this.placeId));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));

        return map;
    }

}
