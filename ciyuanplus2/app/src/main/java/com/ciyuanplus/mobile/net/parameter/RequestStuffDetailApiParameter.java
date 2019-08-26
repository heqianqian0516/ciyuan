package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestStuffDetailApiParameter extends ApiParameter {
    private String communityUuid;
    private final String userUuid;
    private final String userState;
    private final String postUuid;
    private final String longitude;
    private final String latitude;


    public RequestStuffDetailApiParameter(String longitude, String latitude, String postUuid) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
        this.postUuid = postUuid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));

        return map;
    }

}
