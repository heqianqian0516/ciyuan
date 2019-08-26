package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetCommunityUsersListApiParameter extends ApiParameter {

    private final String userUuid;
    private final String communityUuid;
    private final String userState;

    public GetCommunityUsersListApiParameter() {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));

        return map;
    }

}
