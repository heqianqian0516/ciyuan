package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestFreshNewsApiParameter extends ApiParameter {
    private String pager;
    private String pageSize;
    private String userUuid;
    private String loginUserUuid = "";
    private String userState = "";
    private String communityUuid = "";

    public RequestFreshNewsApiParameter(String pager, String pageSize) {
        this.pager = pager;
        this.pageSize = pageSize;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    public RequestFreshNewsApiParameter(String pager, String pageSize, String userUuid) { //请求其他人
        this.pager = pager;
        this.pageSize = pageSize;
        this.userUuid = userUuid;
        this.loginUserUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        this.userState = LoginStateManager.isLogin() ? "1" : "2";
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("loginUserUuid", new ApiParamMap.ParamData(this.loginUserUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        return map;
    }
}
